/*
 * Copyright 2017 Traversal Space
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package space.traversal.kapsule.demo.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import space.traversal.kapsule.Injects
import space.traversal.kapsule.demo.App
import space.traversal.kapsule.demo.R
import space.traversal.kapsule.demo.di.Module
import space.traversal.kapsule.demo.presenter.HomePresenter
import space.traversal.kapsule.demo.presenter.HomeView
import space.traversal.kapsule.inject
import space.traversal.kapsule.required

/**
 * View for [HomePresenter].
 */
class HomeActivity : AppCompatActivity(), HomeView, Injects<Module> {

    private val inflater by required { layoutInflater }

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inject(App.module(this))

        presenter = HomePresenter(this).also { it.attach(this) }

        btn_add.setOnClickListener { presenter.update(+1) }
        btn_remove.setOnClickListener { presenter.update(-1) }

        presenter.load()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.menu_counter -> {
            startActivity(Intent(this, CounterActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun updateCount(count: Int) {
        val current = container_list.childCount
        if (current < count) {
            for (i in 1..count - current) {
                container_list.addView(inflater.inflate(R.layout.view_launcher, container_list, false))
            }
        } else if (current > count) {
            for (i in (current - 1) downTo (count)) {
                container_list.removeViewAt(i)
            }
        }
    }
}
