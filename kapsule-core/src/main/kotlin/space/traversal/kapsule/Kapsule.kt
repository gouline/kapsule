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

/**
 * Kapsule injector.
 *
 * @param <M> Module type that it injects.
 */
class Kapsule<M> {

    internal val delegates = mutableListOf<Delegate<M, *>>()

    /**
     * Initializes property delegates with provided functions.
     *
     * @param module Injection module containing providers.
     */
    @Synchronized fun inject(module: M) {
        delegates.forEach { it.initialize(module) }
    }

    /**
     * Creates and registers delegate for a required (non-null) injectable property.
     *
     * @param initializer Initializer function from the module context to value.
     * @return Required (non-null) property delegate.
     */
    fun <T> required(initializer: M.() -> T) = Delegate.Required(initializer).apply { delegates += this }

    /**
     * Creates and registers delegate for an optional (nullable) injectable property.
     *
     * @param initializer Initializer function from the module context to value.
     * @return Optional (nullable) property delegate.
     */
    fun <T> optional(initializer: M.() -> T?) = Delegate.Optional(initializer).apply { delegates += this }

    /**
     * Shortcut for [required] by invoking the class like a function.
     *
     * @param initializer Initializer function from the module context to value.
     * @return Required (non-null) property delegate.
     */
    operator fun <T> invoke(initializer: M.() -> T) = required(initializer)
}
