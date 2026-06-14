plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
    implementation("com.google.guava:guava:33.0.0-jre")
    implementation("com.google.code.gson:gson:2.10.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.ronobot.engine.App"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.ronobot.engine.App"
    }
}
