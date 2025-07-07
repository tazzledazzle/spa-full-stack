plugins {
    kotlin("multiplatform") version "2.1.0"
}
repositories {
    mavenCentral()
    google()
}

kotlin {
    js {
        browser {

        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(npm("react", "^18.2.0"))
                implementation(npm("react-dom", "^18.2.0"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}