package net.gouline.kapsule.demo.di

/**
 * Test implementation of [PersonModule].
 */
class TestPersonModule : PersonModule {

    override val firstName = "Jane"
    override val lastName = "Doe"
}

/**
 * Test implementation of [ContactsModule].
 */
class TestContactsModule : ContactsModule {

    override val emails = listOf("jdoe@example.org", "jane@example.org")
}
