import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    alias(libs.plugins.dokka)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")

    group = property("publishGroupId").toString()
    version = property("publishVersion").toString()

    tasks.withType<DokkaTaskPartial>().configureEach {
        dokkaSourceSets.configureEach {
            includes.from("README.md")
        }
    }
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(rootProject.file("docs"))
}
