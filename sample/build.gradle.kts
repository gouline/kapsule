plugins {
    application
}

dependencies {
    implementation(project(":kapsule-core"))

    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.mockito.kotlin)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

application {
    mainClass = "net.gouline.kapsule.demo.DemoKt"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
