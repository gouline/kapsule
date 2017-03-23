package space.traversal.kapsule

/**
 * Kapsule injector.
 *
 * @param <M> Module type that it injects.
 */
class Kapsule<M> {

    internal val delegates = mutableListOf<Delegate<M, *>>()

    /**
     * Initializes property delegates with provided functions.
     *
     * @param module Injection module containing providers.
     */
    @Synchronized fun inject(module: M) {
        delegates.forEach { it.initialize(module) }
    }

    /**
     * Creates and registers delegate for a required (non-null) injectable property.
     *
     * @param initializer Initializer function from the module context to value.
     */
    fun <T> req(initializer: M.() -> T) = Delegate(initializer, true).apply { delegates += this }

    /**
     * Creates and registers delegate for an optional (nullable) injectable property.
     *
     * @param initializer Initializer function from the module context to value.
     */
    fun <T> opt(initializer: M.() -> T?) = Delegate(initializer, false).apply { delegates += this }

    /**
     * Shortcut for [req] by invoking the class like a function.
     *
     * @param initializer Initializer function from the module context to value.
     */
    operator fun <T> invoke(initializer: M.() -> T) = req(initializer)
}
