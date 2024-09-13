import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    alias(libs.plugins.maven.publish)
}

dependencies {
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.mockito.kotlin)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

mavenPublishing {
    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
        )
    )

    publishToMavenCentral(SonatypeHost.DEFAULT)

    signAllPublications()

    coordinates(
        artifactId = name,
        groupId = group.toString(),
        version = version.toString(),
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
