package space.traversal.kapsule.demo

import space.traversal.kapsule.Kapsule
import space.traversal.kapsule.demo.di.DemoModule

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

    var firstName by kap { firstName }
    val lastName by kap.opt { lastName }
    val emails by kap { emails }

    init {
        kap.inject(context.module)
    }
}
