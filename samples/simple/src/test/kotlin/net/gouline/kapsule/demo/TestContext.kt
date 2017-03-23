package net.gouline.kapsule.demo

import net.gouline.kapsule.demo.di.DemoModule
import net.gouline.kapsule.demo.di.TestContactsModule
import net.gouline.kapsule.demo.di.TestPersonModule

/**
 * Test implementation of [Context].
 */
class TestContext : Context() {

    override val module = DemoModule(
            person = TestPersonModule(),
            contacts = TestContactsModule())
}
