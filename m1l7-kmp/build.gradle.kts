plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        /*compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }*/
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        browser {}
    }
    // Identify the environment on which we work and, based on the results, determine the target platform
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosArm64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val libcurl by creating
            }
        }
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    val coroutinesVersion: String by project


    // Description of modules corresponding to our target platforms
    //  common - common code that we can use on different platforms
    //  for each target platform, we can specify our own specific dependencies
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        // dependencies from npm
        val jsMain by getting {
            dependencies {
                implementation(npm("js-big-decimal", "~1.3.4"))
                implementation(npm("is-sorted", "~1.0.5"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}
