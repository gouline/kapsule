package space.traversal.kapsule

import kotlin.reflect.KProperty

/**
 * Kapsule injector.
 *
 * @param <M> Module type that it injects.
 */
class Kapsule<M> {

    private val delegates = mutableListOf<KapsuleDelegate<M, *>>()
    private var injected = false

    @Synchronized fun inject(m: M) {
        if (injected) throw IllegalStateException("Already injected")
        delegates.forEach { it.initialize(m) }
        delegates.clear()
        injected = true
    }

    operator fun <T> invoke(f: M.() -> T) = KapsuleDelegate(f).also { delegates += it }
}

/**
 * Kapsule property delegate.
 *
 * @param <M> Module type that injects it.
 * @param <T> Property value type.
 */
class KapsuleDelegate<in M, out T>(val delegate: M.() -> T) {

    private var value: T? = null

    fun initialize(m: M) {
        value = delegate(m)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
            value ?: throw IllegalStateException("Not initialized")
}
