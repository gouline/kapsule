/*
 * Copyright 2017 Mike Gouline
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.gouline.kapsule

import net.gouline.kapsule.util.CallerMap

/**
 * Static storage of [Kapsule] instances.
 */
object Kapsules {

    internal val instances = CallerMap<Injects<*>, Kapsule<*>>()

    /**
     * Retrieves active instance of [Kapsule] or creates a new one.
     *
     * @param caller Injection caller instance, used as lookup key.
     * @return Stored or new instance.
     */
    fun <M> get(caller: Injects<M>) = fetch(caller) ?: Kapsule<M>().apply { instances[caller] = this }

    /**
     * Fetches stored instance.
     *
     * @param caller Injection caller instance, used as lookup key.
     * @return Stored instance or null.
     */
    @Suppress("UNCHECKED_CAST")
    internal fun <M> fetch(caller: Injects<M>) = instances[caller] as? Kapsule<M>
}
