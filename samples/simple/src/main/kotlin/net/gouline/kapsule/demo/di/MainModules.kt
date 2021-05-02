/*
 * Copyright 2017 Mike Gouline
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.gouline.kapsule.demo.di

import net.gouline.kapsule.Injects
import net.gouline.kapsule.demo.mock.Auth
import net.gouline.kapsule.demo.mock.StateDao
import net.gouline.kapsule.demo.mock.UserDao
import net.gouline.kapsule.required

/**
 * Main implementation of [DataModule].
 */
class MainDataModule : DataModule {

    override val stateDao = StateDao()
    override val userDao = UserDao()
}

/**
 * Main implementation of [LogicModule].
 */
class MainLogicModule : LogicModule, Injects<DataModule> {

    private val stateDao by required { stateDao }
    private val userDao by required { userDao }

    override val auth get() = Auth(userDao, stateDao)
}
