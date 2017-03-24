package space.traversal.kapsule

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import kotlin.reflect.KProperty

/**
 * Test case for [Kapsule].
 */
class KapsuleTestCase : TestCase() {

    @Test fun testRequired() {
        val kap = Kapsule<MultiModule>()
        assertEquals(true, kap.req { reqInt }.required)
        assertEquals(true, kap<Int> { reqInt }.required)
        assertEquals(false, kap.opt<Int?> { reqInt }.required)
    }

    @Test fun testDelegates() {
        val kap = Kapsule<MultiModule>()
        for (i in 0..2) {
            val initializer: (MultiModule.() -> Int) = { reqInt }
            kap.req(initializer)
            assertEquals(i + 1, kap.delegates.count())
            assertEquals(initializer, kap.delegates[i].initializer)
        }
        kap.inject(MultiModule("", 1, ""))
        assertEquals(3, kap.delegates.count())
    }

    @Test fun testInject() {
        val kap = Kapsule<MultiModule>()
        val prop = Mockito.mock(KProperty::class.java)

        val optStringDelegate = kap.opt<String?> { optString }
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
        var optString by kap.opt<String?> { optString }
        val reqInt by kap<Int> { reqInt }

        fun inject(module: MultiModule) {
            kap.inject(module)
        }
    }
}