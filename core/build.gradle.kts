plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
    signing
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

    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = property("publishArtifactId").toString()
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = property("publishName").toString()
                description = property("publishDescription").toString()
                url = property("publishUrl").toString()
                licenses {
                    license {
                        name = property("publishLicenseName").toString()
                        url = property("publishLicenseUrl").toString()
                    }
                }
                developers {
                    developer {
                        id = property("publishDeveloperId").toString()
                        name = property("publishDeveloperName").toString()
                    }
                }
                scm {
                    connection = property("publishScmConnection").toString()
                    developerConnection = property("publishScmDeveloperConnection").toString()
                    url = property("publishScmUrl").toString()
                }
            }
        }
    }

    repositories {
        if (hasProperty("sonatypeUsername") && hasProperty("sonatypePassword")) {
            maven {
                name = "sonatype"
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

                credentials {
                    username = findProperty("sonatypeUsername")?.toString()
                    password = findProperty("sonatypePassword")?.toString()
                }
            }
        }
    }
}

signing {
    if (hasProperty("signingKey") && hasProperty("signingPassphrase")) {
        val signingKey: String? by project
        val signingPassphrase: String? by project
        useInMemoryPgpKeys(signingKey, signingPassphrase)
        sign(publishing.publications["mavenJava"])
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
