plugins {
    id("java")
}

group = "org.screenplay"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val serenityVersion = "4.1.20"

dependencies {
    // Serenity BDD core + Cucumber integration
    testImplementation("net.serenity-bdd:serenity-core:$serenityVersion")
    testImplementation("net.serenity-bdd:serenity-cucumber:$serenityVersion")

    // Screenplay pattern
    testImplementation("net.serenity-bdd:serenity-screenplay:$serenityVersion")
    testImplementation("net.serenity-bdd:serenity-screenplay-rest:$serenityVersion")

    // REST Assured support
    testImplementation("net.serenity-bdd:serenity-rest-assured:$serenityVersion")

    // Assertions
    testImplementation("org.assertj:assertj-core:3.24.2")

    // Force ByteBuddy ≥ 1.15 for Java 21+ compatibility (Serenity may pull older versions)
    testImplementation("net.bytebuddy:byte-buddy:1.15.11")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.15.11")

    // JNA required by byte-buddy-agent on Windows (classpath scan by Cucumber would fail otherwise)
    testImplementation("net.java.dev.jna:jna:5.13.0")
    testImplementation("net.java.dev.jna:jna-platform:5.13.0")

    // JUnit Platform Suite — needed to run the @Suite Cucumber runner
    testImplementation("org.junit.platform:junit-platform-suite:1.10.3")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.17.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    // Allow ByteBuddy to attach its agent dynamically (required on Java 21+)
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    testLogging {
        showStandardStreams = true
        events("passed", "failed", "skipped")
        showExceptions = true
        showCauses = true
        showStackTraces = false
    }
}