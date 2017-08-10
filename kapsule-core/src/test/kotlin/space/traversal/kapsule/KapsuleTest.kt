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
 * Test case for [Kapsule].
 */
class KapsuleTest : TestCase() {

    @Test fun testRequired() {
        val kap = Kapsule<MultiModule>()
        assertTrue(kap.required { reqInt } is Delegate.Required)
        assertTrue(kap<Int> { reqInt } is Delegate.Required)
        assertTrue(kap.optional<Int?> { reqInt } is Delegate.Optional)
    }

    @Test fun testDelegates() {
        val kap = Kapsule<MultiModule>()
        for (i in 0..2) {
            val initializer: (MultiModule.() -> Int) = { reqInt }
            kap.required(initializer)
            assertEquals(i + 1, kap.delegates.count())
            assertEquals(initializer, kap.delegates[i].initializer)
        }
        kap.inject(MultiModule("", 1, ""))
        assertEquals(3, kap.delegates.count())
    }

    @Test fun testInject() {
        val kap = Kapsule<MultiModule>()
        val prop = Mockito.mock(KProperty::class.java)

        val optStringDelegate = kap.optional<String?> { optString }
        val reqIntDelegate = kap<Int> { reqInt }

        listOf(MultiModule("test1", 3, "abc123"),
                MultiModule("test2", 7, "xyz890")).forEach { module ->
            kap.inject(module)
            assertEquals(module.optString, optStringDelegate.getValue(null, prop))
            assertEquals(module.reqInt, reqIntDelegate.getValue(null, prop))
        }
    }

    @Test fun testTarget_pre() {
        val target = Target()

        assertEquals(null, target.optString)

        try {
            target.reqInt
            assertTrue(false)
        } catch (e: KotlinNullPointerException) {
            assertTrue(true)
        }
    }

    @Test fun testTarget_multiple() {
        val target = Target()

        listOf(MultiModule("test1", 3, "abc123"),
                MultiModule(null, 7, "xyz890")).forEach { module ->
            target.inject(module)
            assertEquals(module.optString, target.optString)
            assertEquals(module.reqInt, target.reqInt)
        }
    }

    class Target {

        val kap = Kapsule<MultiModule>()
        var optString by kap.optional<String?> { optString }
        val reqInt by kap<Int> { reqInt }

        fun inject(module: MultiModule) {
            kap.inject(module)
        }
    }
}
