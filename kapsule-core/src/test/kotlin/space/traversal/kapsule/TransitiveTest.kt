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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test case for transitive dependencies.
 */
class TransitiveTest {

    @Test fun testCast_legal() {
        try {
            Module1(
                    object : ModuleA {
                        override val stringA = "A"
                    },
                    object : ModuleB {
                        val stringA by required { stringA }
                        override val stringB get() = "${stringA}B"
                    }
            ).transitive()
            assertTrue(true)
        } catch (e: TransitiveInjectionException) {
            assertTrue(false)
        }
    }

    @Test fun testCast_illegal() {
        try {
            Module2(
                    object : ModuleA {
                        override val stringA = "A"
                    },
                    object : ModuleC {
                        val stringA by required { stringA }
                        override val stringC get() = "${stringA}C"
                    }
            ).transitive()
            assertTrue(false)
        } catch (e: TransitiveInjectionException) {
            assertTrue(true)
        }
    }

    @Test fun testCast_valid() {
        val a = object : ModuleA {
            override val stringA = "A"
        }
        val b = object : ModuleB {
            val stringA by required { stringA }
            override val stringB get() = "${stringA}B"
        }
        Module1(a, b).transitive()
        assertEquals("A", a.stringA)
        assertEquals("A", b.stringA)
        assertEquals("AB", b.stringB)
    }
}

interface ModuleA {
    val stringA: String
}

interface ModuleB : Injects<ModuleA> {
    val stringB: String
}

interface ModuleC : Injects<RogueModule> {
    val stringC: String
}

interface RogueModule {
    val stringA: String
}

class Module1(
        a: ModuleA,
        b: ModuleB) :
        ModuleA by a,
        ModuleB by b,
        HasModules {

    override val modules = setOf(a, b)
}

class Module2(
        a: ModuleA,
        c: ModuleC) :
        ModuleA by a,
        ModuleC by c,
        HasModules {

    override val modules = setOf(a, c)
}
