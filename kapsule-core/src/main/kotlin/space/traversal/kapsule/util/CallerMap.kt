/*
 * Copyright 2017 Traversal Space
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package space.traversal.kapsule.util

import java.util.*

/**
 * Utility map for storing caller instances.
 *
 * Implements a cache such that the most recent entry is returned at constant time.
 */
internal class CallerMap<K, V> : WeakHashMap<K, V>() {

    @Volatile internal var lastKey: K? = null
    @Volatile internal var lastValue: V? = null

    override fun containsKey(key: K) = key == lastKey || super.containsKey(key)

    override fun containsValue(value: V): Boolean {
        return super.containsValue(value)
    }

    operator override fun get(key: K): V? {
        return if (key == lastKey) {
            lastValue
        } else {
            val value = super.get(key)
            setLast(key, value)
            return value
        }
    }

    operator fun set(key: K, value: V) = put(key, value)

    override fun put(key: K, value: V): V? {
        setLast(key, value)
        return super.put(key, value)
    }

    override fun remove(key: K): V? {
        if (key == lastKey) {
            setLast()
        }
        return super.remove(key)
    }

    override fun clear() {
        super.clear()
        setLast()
    }

    /**
     * Set last key-value pair cache.
     *
     * @param key Map key.
     * @param value Map value.
     */
    private fun setLast(key: K? = null, value: V? = null) {
        lastKey = key
        lastValue = value
    }
}
