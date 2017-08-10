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

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import kotlin.reflect.KProperty

/**
 * Test case for [Delegate].
 */
class DelegateTest : TestCase() {

    @Test fun testInitialize_required() {
        val delegate = Delegate.Required<RequiredModule, String> { value }
        assertEquals(null, delegate.value)

        listOf("one", "two").forEach { value ->
            val module = RequiredModule(value)
            delegate.initialize(module)
            assertEquals(value, delegate.value)
        }
    }

    @Test fun testInitialize_optional() {
        val delegate = Delegate.Optional<OptionalModule, String?> { value }
        assertEquals(null, delegate.value)

        listOf(null, "three").forEach { value ->
            val module = OptionalModule(value)
            delegate.initialize(module)
            assertEquals(value, delegate.value)
        }
    }

    @Test fun testGetValue_required() {
        val delegate = Delegate.Required<RequiredModule, String> { value }
        val prop = Mockito.mock(KProperty::class.java)

        try {
            delegate.getValue(null, prop)
            assertTrue(false)
        } catch (e: KotlinNullPointerException) {
            assertTrue(true)
        }

        val expected = "test1"
        delegate.value = expected
        assertEquals(expected, delegate.getValue(null, prop))
    }

    @Test fun testGetValue_optional() {
        val delegate = Delegate.Optional<OptionalModule, String?> { value }
        val prop = Mockito.mock(KProperty::class.java)

        assertEquals(null, delegate.getValue(null, prop))

        val expected = "test2"
        delegate.value = expected
        assertEquals(expected, delegate.getValue(null, prop))
    }

    @Test fun testSetValue_required() {
        val delegate = Delegate.Required<RequiredModule, String> { value }
        val prop = Mockito.mock(KProperty::class.java)

        listOf("test3", "test4").forEach { expected ->
            delegate.setValue(null, prop, expected)
            assertEquals(expected, delegate.value)
        }
    }

    @Test fun testSetValue_optional() {
        val delegate = Delegate.Optional<OptionalModule, String?> { value }
        val prop = Mockito.mock(KProperty::class.java)

        listOf("test3", null).forEach { expected ->
            delegate.setValue(null, prop, expected)
            assertEquals(expected, delegate.value)
        }
    }
}
