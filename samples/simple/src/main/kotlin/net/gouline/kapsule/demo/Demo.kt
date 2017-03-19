package net.gouline.kapsule.demo

import net.gouline.kapsule.Kapsule

fun main(args: Array<String>) {
    val act = Activity()
}

object Application {

    val module = DemoModule(
            id = MainIdModule(),
            url = MainUrlModule())

    val testModule = DemoModule(
            id = TestIdModule(),
            url = MainUrlModule())
}

class Activity {

    private val kap = Kapsule<DemoModule>()

    var id by kap.opt<Int?> { id }
    val name by kap<String> { name }
    val url by kap<String> { url }

    init {
        kap.inject(Application.module)
        println("act1: $id, $name, $url")

        id = null
        println("act2: $id, $name, $url")

        kap.inject(Application.testModule)
        println("act3: $id, $name, $url")
    }
}

class DemoModule(id: IdModule, url: UrlModule) : IdModule by id, UrlModule by url

interface IdModule {
    val id: Int
    val name: String
}

interface UrlModule {
    val url: String
}

class MainIdModule : IdModule {
    override val id = 42
    override val name = "Developer"
}

class MainUrlModule : UrlModule {
    override val url = "https://github.com"
}

class TestIdModule : IdModule {
    override val id = 24
    override val name = "Tester"
}
