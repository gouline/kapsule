package net.gouline.kapsule

import junit.framework.TestCase
import org.junit.Test

/**
 * Test case for [Delegate].
 */
class DelegateTestCase : TestCase() {

    @Test fun testInitialize_required() {
        val delegate = Delegate<RequiredModule, String>({ value }, true)
        assert(delegate.value == null)

        val module1 = RequiredModule("one")
        delegate.initialize(module1)
        assertEquals(module1.value, delegate.value)

        val module2 = RequiredModule("two")
        delegate.initialize(module2)
        assertEquals(module2.value, delegate.value)
    }

    @Test fun testInitialize_optional() {
        val delegate = Delegate<OptionalModule, String?>({ value }, true)
        assert(delegate.value == null)

        val module1 = OptionalModule(null)
        delegate.initialize(module1)
        assertEquals(module1.value, delegate.value)

        val module2 = OptionalModule("three")
        delegate.initialize(module2)
        assertEquals(module2.value, delegate.value)
    }

    class RequiredModule(val value: String)

    class OptionalModule(val value: String?)
}