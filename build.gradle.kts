plugins {
    kotlin("multiplatform") version "2.2.0"
}
repositories {
    mavenCentral()
    google()
}

kotlin {
    js {
        browser {

            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
            nodejs {
                version = "18.16.0"
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(npm("react", "^18.2.0"))
                implementation(npm("react-dom", "^18.2.0"))
                implementation(npm("vite", "5.0.0"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
