/*
 * Copyright 2017 Traversal Space
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package space.traversal.kapsule.demo.presenter

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import space.traversal.kapsule.Kapsule
import space.traversal.kapsule.demo.App
import space.traversal.kapsule.demo.di.Module
import space.traversal.kapsule.demo.test.TestRunner

/**
 * Test for [HomePresenter].
 */
@RunWith(TestRunner::class)
class HomePresenterTest {

    private val kap = Kapsule<Module>()
    private val dao by kap { dao }

    init {
        kap.inject(App.module(RuntimeEnvironment.application))
    }

    @Test fun testLoad() {
        var updatedViewCount = 0
        val view = object : HomeView {
            override fun updateCount(count: Int) {
                updatedViewCount = count
            }

        }
        val presenter = HomePresenter(RuntimeEnvironment.application).also { it.attach(view) }
        dao.persistCount(3)
        presenter.load()
        assertEquals(3, updatedViewCount)
    }

    @Test fun testIncrement() {
        var updatedViewCount = 0
        val view = object : HomeView {
            override fun updateCount(count: Int) {
                updatedViewCount = count
            }

        }
        val presenter = HomePresenter(RuntimeEnvironment.application).also { it.attach(view) }
        dao.persistCount(3)
        presenter.load()
        presenter.update(+1)
        assertEquals(4, updatedViewCount)
    }

    @Test fun testDecrement() {
        var updatedViewCount = 0
        val view = object : HomeView {
            override fun updateCount(count: Int) {
                updatedViewCount = count
            }

        }
        val presenter = HomePresenter(RuntimeEnvironment.application).also { it.attach(view) }
        dao.persistCount(3)
        presenter.load()
        presenter.update(-1)
        assertEquals(2, updatedViewCount)
    }
}
