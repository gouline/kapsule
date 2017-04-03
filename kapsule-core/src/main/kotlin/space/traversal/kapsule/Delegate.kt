/*
 * Copyright 2017 Traversal Space
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package space.traversal.kapsule

import kotlin.reflect.KProperty

/**
 * Kapsule property delegate.
 *
 * @param <M> Module type that injects it.
 * @param <T> Property value type.
 * @param initializer Value initializer function.
 */
sealed class Delegate<in M, T>(internal val initializer: M.() -> T?) {

    var value: T? = null
        internal set

    /**
     * Initializes value from the injection module.
     *
     * @param module Injection module containing provider.
     */
    fun initialize(module: M) {
        value = initializer(module)
    }

    /**
     * Delegate for required (non-null) values.
     */
    class Required<in M, T>(initializer: M.() -> T) : Delegate<M, T>(initializer) {

        /**
         * Delegate for value reads.
         */
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = value!!

        /**
         * Delegate for value writes.
         */
        operator fun setValue(thisRef: Any?, property: KProperty<*>, t: T) {
            value = t
        }
    }

    /**
     * Delegate for optional (nullable) values.
     */
    class Optional<in M, T>(initializer: M.() -> T?) : Delegate<M, T>(initializer) {

        /**
         * Delegate for value reads.
         */
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = value

        /**
         * Delegate for value writes.
         */
        operator fun setValue(thisRef: Any?, property: KProperty<*>, t: T?) {
            value = t
        }
    }
}
