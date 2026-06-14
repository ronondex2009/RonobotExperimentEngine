plugins {
    id("java")
    id("application")
    id("org.jetbrains.dokka-javadoc") version "2.2.0"
}

group = "org.ronobot.engine"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // JUnit Jupiter for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")
    
    // Kotlin support (for build script)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.21")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

application {
    mainClass = "org.ronobot.engine.App"
    
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
}
