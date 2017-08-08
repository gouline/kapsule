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

import java.lang.ClassCastException
import java.util.*

/**
 * Module with other submodules attached via delegation.
 */
interface HasModules {

    /**
     * Set of child modules. Used for transitive injection.
     */
    val modules: Set<Any>
}

/**
 * Injects root module into any submodules with transitive dependencies.
 *
 * @return Root module for chaining.
 * @param <M> Module type, must have submodules.
 */
fun <M : HasModules> M.transitive(): M {
    val queue = ArrayDeque<Any?>()
    queue.offer(this)
    while (queue.isNotEmpty()) {
        val submodule = queue.poll()!!
        try {
            @Suppress("UNCHECKED_CAST")
            (submodule as? Injects<M>)?.inject(this)
            (submodule as? HasModules)?.let { queue.addAll(it.modules) }
        } catch (e: ClassCastException) {
            throw TransitiveInjectionException(submodule::class.java)
        }
    }
    return this
}

/**
 * Class cast exception indicating that a submodule depends on a module that
 * is not a descendant of the root module.
 *
 * @param
 */
class TransitiveInjectionException(clazz: Class<*>) : ClassCastException("Unexpected transitive injection: ${clazz.name}")
