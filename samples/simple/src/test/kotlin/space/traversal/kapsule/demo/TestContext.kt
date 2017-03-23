package space.traversal.kapsule.demo

import space.traversal.kapsule.demo.di.DemoModule
import space.traversal.kapsule.demo.di.TestContactsModule
import space.traversal.kapsule.demo.di.TestPersonModule

/**
 * Test implementation of [Context].
 */
class TestContext : Context() {

    override val module = DemoModule(
            person = TestPersonModule(),
            contacts = TestContactsModule())
}
