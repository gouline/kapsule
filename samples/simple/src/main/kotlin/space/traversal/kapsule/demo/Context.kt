package space.traversal.kapsule.demo

import space.traversal.kapsule.demo.di.DemoModule
import space.traversal.kapsule.demo.di.MainContactsModule
import space.traversal.kapsule.demo.di.MainPersonModule

/**
 * Basic concept of application "context".
 */
open class Context {

    val domain = "example.com"

    open val module = DemoModule(
            person = MainPersonModule(),
            contacts = MainContactsModule(this))
}