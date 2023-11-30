plugins {
    kotlin("jvm") version "1.9.0"
    application
    // Plugin for Dokka - KDoc generating tool
    id("org.jetbrains.dokka") version "1.9.10"
    // Code coverage tool
    jacoco
    // Plugin for Ktlint
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

group = "me.karinegassanova"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.20-RC")
    // dependencies for logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    // dependency for JSON Persistence
    implementation("com.thoughtworks.xstream:xstream:1.4.20")
    implementation("org.codehaus.jettison:jettison:1.5.4")
    // For generating a Dokka Site from KDoc
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.9.10")
}

tasks.test {
    useJUnitPlatform()
    // report is always generated after tests run
    finalizedBy(tasks.jacocoTestReport)
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "MainKt"
    // for building a fat jar - include all dependencies
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
