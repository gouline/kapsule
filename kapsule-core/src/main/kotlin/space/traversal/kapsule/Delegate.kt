package space.traversal.kapsule

import kotlin.reflect.KProperty

/**
 * Kapsule property delegate.
 *
 * @param <M> Module type that injects it.
 * @param <T> Property value type.
 * @param initializer Value initializer function.
 */
sealed class Delegate<in M, T>(internal val initializer: M.() -> T?) {

    internal var value: T? = null

    /**
     * Initializes value from the injection module.
     *
     * @param module Injection module containing provider.
     */
    fun initialize(module: M) {
        value = initializer(module)
    }

    /**
     * Delegate for required (non-null) values.
     */
    class Required<in M, T>(initializer: M.() -> T) : Delegate<M, T>(initializer) {

        /**
         * Delegate for value reads.
         */
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = value!!

        /**
         * Delegate for value writes.
         */
        operator fun setValue(thisRef: Any?, property: KProperty<*>, t: T) {
            value = t
        }
    }

    /**
     * Delegate for optional (nullable) values.
     */
    class Optional<in M, T>(initializer: M.() -> T?) : Delegate<M, T>(initializer) {

        /**
         * Delegate for value reads.
         */
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = value

        /**
         * Delegate for value writes.
         */
        operator fun setValue(thisRef: Any?, property: KProperty<*>, t: T?) {
            value = t
        }
    }
}
