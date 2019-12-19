/*
 * Copyright 2017 Traversal.space
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package space.traversal.kapsule.demo.di

import space.traversal.kapsule.HasModules
import space.traversal.kapsule.demo.mock.Auth
import space.traversal.kapsule.demo.mock.StateDao
import space.traversal.kapsule.demo.mock.UserDao

/**
 * Application module.
 */
class Module(
        data: DataModule,
        logic: LogicModule) :
        DataModule by data,
        LogicModule by logic,
        HasModules {

    override val modules = setOf(data, logic)
}

/**
 * Module for data providers.
 */
interface DataModule {

    val userDao: UserDao
    val stateDao: StateDao
}

/**
 * Module for logic objects.
 */
interface LogicModule {

    val auth: Auth
}
