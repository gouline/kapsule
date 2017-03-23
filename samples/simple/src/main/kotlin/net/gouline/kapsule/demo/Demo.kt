package net.gouline.kapsule.demo

import net.gouline.kapsule.Kapsule
import net.gouline.kapsule.demo.di.DemoModule

fun main(args: Array<String>) {
    val demo = Demo(Context())
    println("First name: ${demo.firstName}")
    println("Last name: ${demo.lastName}")
    println("Emails: ${demo.emails}")
}

/**
 * Demo app definition.
 */
class Demo(context: Context) {

    private val kap = Kapsule<DemoModule>()

    val firstName by kap { firstName }
    val lastName by kap.opt { lastName }
    val emails by kap { emails }

    init {
        kap.inject(context.module)
    }
}
