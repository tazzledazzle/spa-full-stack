val h2_version: String by project
val kotlin_version: String by project
val kotlinx_html_version: String by project
val logback_version: String by project
val postgres_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "com.northshore"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

//    val isDevelopment: Boolean = project.ext.has("development")
    val isDevelopment: Boolean = true
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
sourceSets {
    test {
        kotlin.srcDir("src/test/kotlin")
    }
}

val junit_ver = "5.12.0"
dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-pebble")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-metrics")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    // Add these lines:
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-server-html-builder")
    implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinx_html_version")
    implementation("org.jetbrains:kotlin-css:1.0.0-pre.129-kotlin-1.4.20")
    implementation("io.ktor:ktor-server-thymeleaf")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-netty")
    // For form handling
    implementation("io.ktor:ktor-server-html-builder:2.3.8")
    implementation("io.ktor:ktor-server-sessions:2.3.8")
    implementation("io.ktor:ktor-server-status-pages:2.3.8")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    // Validation
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("org.glassfish:jakarta.el:5.0.0-M1")

    // Use a BOM (Bill of Materials) to ensure consistent versions
    testImplementation(platform("org.junit:junit-bom:$junit_ver"))

    // Then declare dependencies without versions
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.platform:junit-platform-launcher:1.12.0")

    // Other test dependencies...
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
tasks.named<Test>("test") {
    useJUnitPlatform()
}


configurations.all {
    resolutionStrategy {
        force("org.junit.jupiter:junit-jupiter-engine:$junit_ver")
        force("org.junit.jupiter:junit-jupiter-api:$junit_ver")
    }
}
