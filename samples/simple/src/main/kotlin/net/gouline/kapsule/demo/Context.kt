package net.gouline.kapsule.demo

import net.gouline.kapsule.demo.di.DemoModule
import net.gouline.kapsule.demo.di.MainContactsModule
import net.gouline.kapsule.demo.di.MainPersonModule

/**
 * Basic concept of application "context".
 */
open class Context {

    val domain = "example.com"

    open val module = DemoModule(
            person = MainPersonModule(),
            contacts = MainContactsModule(this))
}