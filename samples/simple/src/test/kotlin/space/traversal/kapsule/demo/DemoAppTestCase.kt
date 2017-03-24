package space.traversal.kapsule.demo

import junit.framework.TestCase
import org.junit.Test

/**
 * Test case for [Demo].
 */
class DemoAppTestCase : TestCase() {

    private val testContext = TestContext()

    @Test fun testInjection() {
        val demo = Demo(testContext)
        assertEquals("Jane", demo.firstName)
        assertEquals("Doe", demo.lastName)
        assertTrue(demo.emails.contains("jdoe@example.org"))
        assertTrue(demo.emails.contains("jane@example.org"))
    }
}