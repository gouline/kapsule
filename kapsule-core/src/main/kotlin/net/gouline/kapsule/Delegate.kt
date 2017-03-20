package net.gouline.kapsule

import kotlin.reflect.KProperty

/**
 * Kapsule property delegate.
 *
 * @param <M> Module type that injects it.
 * @param <T> Property value type.
 * @param initializer Value initializer function.
 * @param required True for required (non-null), otherwise optional (nullable).
 */
class Delegate<in M, T>(internal val initializer: M.() -> T, internal val required: Boolean = false) {

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
     * Delegate for value reads.
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = if (required) value!! else value

    /**
     * Delegate for value writes.
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, t: T) {
        value = t
    }
}
