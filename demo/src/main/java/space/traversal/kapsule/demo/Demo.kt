package space.traversal.kapsule.demo

import space.traversal.kapsule.Kapsule

fun main(args: Array<String>) {
    val act = Activity()
    println("act: ${act.id}, ${act.name}")
}

object Application {

    val module = MainModule()
}

class Activity {

    private val kap = Kapsule<MainModule>()

    val id by kap { id }
    val name by kap { name }

    init {
        kap.inject(Application.module)
    }
}

open class MainModule {

    open val id = 42

    open val name = "main injected stuff"
}

class TestModule : MainModule() {

    override val id = 24

    override val name = "test injected stuff"
}
