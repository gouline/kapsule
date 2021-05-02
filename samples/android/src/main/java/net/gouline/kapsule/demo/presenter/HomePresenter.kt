/*
 * Copyright 2017 Mike Gouline
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.gouline.kapsule.demo.presenter

import android.content.Context
import net.gouline.kapsule.Injects
import net.gouline.kapsule.demo.App
import net.gouline.kapsule.demo.di.Module
import net.gouline.kapsule.inject
import net.gouline.kapsule.required

/**
 * Presenter for home screen.
 */
class HomePresenter(context: Context) : Presenter<HomeView>(), Injects<Module> {

    private val dao by required { dao }

    init {
        inject(App.module(context))
    }

    /**
     * Loads initial count and refreshes view.
     */
    fun load() {
        val count = dao.fetchCount()
        applyView { updateCount(count) }
    }

    /**
     * Updates count with delta and refreshes view.
     */
    fun update(delta: Int) {
        val count = Math.min(Math.max(dao.fetchCount() + delta, 0), 5)
        dao.persistCount(count)
        applyView { updateCount(count) }
    }
}

/**
 * View for home screen.
 */
interface HomeView : View {

    /**
     * Refreshes view with new count.
     */
    fun updateCount(count: Int)
}
