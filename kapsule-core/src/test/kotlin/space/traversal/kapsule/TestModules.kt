package space.traversal.kapsule

/**
 * Test module with one required (non-null) value.
 */
class RequiredModule(val value: String)

/**
 * Test module with one optional (nullable) value.
 */
class OptionalModule(val value: String?)

/**
 * Test module with multiple values.
 */
class MultiModule(val optString: String?, val reqInt: Int, val unused: String)
