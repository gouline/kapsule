import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.dokka)
}

group = property("publishGroupId").toString()
version = property("publishVersion").toString()

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.mockito.kotlin)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

mavenPublishing {
    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
        )
    )

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(
        property("publishGroupId").toString(),
        property("publishArtifactId").toString(),
        property("publishVersion").toString(),
    )

    pom {
        name.set(property("publishName").toString())
        description.set(property("publishDescription").toString())
        url.set(property("publishUrl").toString())
        licenses {
            license {
                name.set(property("publishLicenseName").toString())
                url.set(property("publishLicenseUrl").toString())
            }
        }
        developers {
            developer {
                id.set(property("publishDeveloperId").toString())
                name.set(property("publishDeveloperName").toString())
            }
        }
        scm {
            connection.set(property("publishScmConnection").toString())
            developerConnection.set(property("publishScmDeveloperConnection").toString())
            url.set(property("publishScmUrl").toString())
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<Copy>("exportDocs") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    into("../docs")
    with(copySpec {
        from("build/dokka/html")
    })
    from(tasks.getByName("deleteDocs"), tasks.dokkaHtml)
}

tasks.register<Delete>("deleteDocs") {
    delete("../docs")
}
