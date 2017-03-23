package net.gouline.kapsule.demo.di

import net.gouline.kapsule.demo.Context

/**
 * Main implementation of [PersonModule].
 */
class MainPersonModule : PersonModule {

    override val firstName = "Joe"
    override val lastName = "Bloggs"
}

/**
 * Main implementation of [ContactsModule].
 */
class MainContactsModule(context: Context) : ContactsModule {

    /**
     * List of domain-specific email addresses.
     */
    override val emails = listOf(
            "jbloggs@${context.domain}",
            "joe@${context.domain}")
}
