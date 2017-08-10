/*
 * Copyright 2017 Traversal Space
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package space.traversal.kapsule.demo.mock

/**
 * Simulated DAO for user data.
 */
class UserDao {

    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
}

/**
 * Simulated DAO for state data.
 */
class StateDao {

    var code: Int? = 0
}

/**
 * Simulated authenticator.
 */
class Auth(
        val userDao: UserDao,
        val stateDao: StateDao,
        val debug: Boolean = false) {

    /**
     * Simulated authentication routine.
     */
    fun authenticate() {
        if (debug) return

        with(userDao) {
            firstName = "Joe"
            lastName = "Bloggs"
            email = "jbloggs@example.com"
        }
        stateDao.code = 1
    }
}
