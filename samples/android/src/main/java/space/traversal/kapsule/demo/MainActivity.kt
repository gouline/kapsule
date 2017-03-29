/*
 * Copyright 2017 Traversal Space
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package space.traversal.kapsule.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import space.traversal.kapsule.Kapsule
import space.traversal.kapsule.demo.di.Module

/**
 * Main and only activity.
 */
class MainActivity : AppCompatActivity() {

    private companion object {
        private val KEY_COUNT = "count"
    }

    private val kap = Kapsule<Module>()
    private val inflater by kap { layoutInflater }
    private val prefs by kap { sharedPreferences }
    private val logging by kap { enableLogging }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        kap.inject(App.module(this))

        init(retrieveCount())

        btn_add.setOnClickListener { add() }
        btn_remove.setOnClickListener { remove() }
    }

    /**
     * Initializes the view with count.
     *
     * @param count Initial count of launchers.
     */
    private fun init(count: Int) {
        if (logging) Log.i("init", "count = $count")

        for (i in 0..count - 1) add(false)
    }

    /**
     * Adds launcher view to the list.
     *
     * @param persist True to persist change, false to only change view.
     */
    private fun add(persist: Boolean = true) {
        val view = inflater.inflate(R.layout.view_launcher, container_list, false)
        container_list.addView(view)

        if (persist) persistCount(1)
    }

    /**
     * Removes launcher view from the list.
     *
     * @param persist True to persist change, false to only change view.
     */
    private fun remove(persist: Boolean = true) {
        val count = container_list.childCount
        if (count > 0) {
            container_list.removeViewAt(count - 1)

            if (persist) persistCount(-1)
        }
    }

    /**
     * Persists count to preferences.
     *
     * @param delta Change delta to add to existing.
     */
    private fun persistCount(delta: Int) {
        prefs.edit().putInt(KEY_COUNT, retrieveCount() + delta).apply()
    }

    /**
     * Retrieves count from preferences.
     *
     * @return Retrieved count.
     */
    private fun retrieveCount() = prefs.getInt(KEY_COUNT, 0)
}
