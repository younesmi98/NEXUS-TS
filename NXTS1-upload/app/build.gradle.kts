import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.tasks.Copy

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Reuse TS3X's protocol/session/audio services without its original Activity/UI.
// Keep generated copies under build/ so the pinned upstream tree stays pristine.
val ts3xSupportOutput = layout.buildDirectory.dir("generated/source/ts3xSupport")
val prepareTs3xSupport by tasks.registering(Copy::class) {
    from(rootProject.file("upstream/ts3x-client/app/src/main/java"))
    include("com/morsen/ts3xclient/*.kt")
    exclude("com/morsen/ts3xclient/MainActivity.kt")
    into(ts3xSupportOutput)
}

android {
    namespace = "com.morsen.ts3xclient"
    compileSdk = 35

    sourceSets.getByName("main").java.srcDir(ts3xSupportOutput)

    defaultConfig {
        applicationId = "com.younesmi98.nexusts"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-final1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_17) } }

dependencies {
    implementation(project(":protocol"))
    implementation(project(":ts3j"))

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.work:work-runtime:2.11.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.8")

    implementation("com.plasmoverse:concentus:1.0.0")
}

// Android's preBuild phase runs before Kotlin compilation and resource processing.
tasks.named("preBuild").configure { dependsOn(prepareTs3xSupport) }
