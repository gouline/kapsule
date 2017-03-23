package space.traversal.kapsule.demo.di

/**
 * Application-wide module that combines all submodules.
 */
class DemoModule(
        person: PersonModule,
        contacts: ContactsModule) :
        PersonModule by person,
        ContactsModule by contacts

/**
 * Person module, provides basic user information.
 */
interface PersonModule {
    val firstName: String
    val lastName: String? // This one is optional
}

/**
 * Contacts module, provides user contact information.
 */
interface ContactsModule {
    val emails: List<String>
}
