plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.karinegassanova"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.20-RC")
    // dependencies for logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    // dependency for YAML Persistence
    implementation("org.yaml:snakeyaml:1.28")
    testImplementation("junit:junit:4.13.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}