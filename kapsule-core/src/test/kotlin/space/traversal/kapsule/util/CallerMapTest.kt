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

import junit.framework.TestCase
import org.junit.Test

/**
 * Test case for [CallerMap].
 */
class CallerMapTest : TestCase() {

    @Test fun testPut() {
        val map = CallerMap<String, String>()
        assertEquals(null, map.lastKey)
        assertEquals(null, map.lastValue)

        map["key1"] = "value1"
        assertEquals("key1", map.lastKey)
        assertEquals("value1", map.lastValue)

        map["key2"] = "value2"
        assertEquals("key2", map.lastKey)
        assertEquals("value2", map.lastValue)
    }

    @Test fun testGet() {
        val map = CallerMap<String, String>()

        map["key1"] = "value1"
        map["key2"] = "value2"
        map["key1"]
        assertEquals("key1", map.lastKey)
        assertEquals("value1", map.lastValue)
    }

    @Test fun testRemove() {
        val map = CallerMap<String, String>()

        map["key1"] = "value1"
        map.remove("key1")
        assertEquals(null, map.lastKey)
        assertEquals(null, map.lastValue)
    }

    @Test fun testClear() {
        val map = CallerMap<String, String>()

        map["key1"] = "value1"
        map.clear()
        assertEquals(null, map.lastKey)
        assertEquals(null, map.lastValue)
    }
}
