import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    id("io.kotest") version "6.0.0.M2"
}

group = "com.northshore"
version = "0.0.1"

application {
    mainClass.set("com.northshore.ApplicationKt")

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


val junit_ver = "5.12.0"
val ktorVersion = "3.1.1"
val koinVersion = "3.2.0"
val logbackVersion = "1.4.12"
val kotlinVersion = "2.1.10"
val kotestVersion = "6.0.0.M2"


dependencies {
    // Ktor core dependencies
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-default-headers:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
//    implementation("io.ktor:ktor-server-static-content:3.0.0")

    // Pebble template engine
    implementation("io.pebbletemplates:pebble:3.2.1")
    implementation("io.ktor:ktor-server-pebble:$ktorVersion")

    // jcharts
    implementation(files("src/jCharts-0.7.5/jCharts-0.7.5.jar"))
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.9.2")
    // Lets-Plot Multiplatform (Batik rendering)
    implementation("org.jetbrains.lets-plot:lets-plot-batik:4.5.2")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:4.6.1")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:4.0.0")
    // Dependency Injection
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // iText PDF 7 for PDF generation
    implementation("com.itextpdf:itext7-core:9.0.0")

    // JFreeChart for chart generation
    implementation("org.jfree:jfreechart:1.5.5")
    // itext7
    implementation ("com.itextpdf:kernel:7.2.5")
    implementation ("com.itextpdf:layout:7.2.5")

    // apache poi
    implementation("org.apache.poi:poi:5.4.0")         // Core POI library
    implementation("org.apache.poi:poi-ooxml:5.4.0")   // OOXML support for handling XLSX files

    // Testing
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-engine-jvm:$kotestVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
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
