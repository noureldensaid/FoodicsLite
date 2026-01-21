plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.foodics.nour"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.foodics.nour"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // UI: Jetpack Compose & Material
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3.window.size)

    // UI: Images & Animations
//    implementation(libs.coil.kt.coil.compose)
//    implementation(libs.coil.gif)
//    implementation(libs.lottie.compose)

    // DI
    implementation(libs.bundles.koin)
    ksp(libs.koin.ksp.compiler)


    // Logging
    implementation(libs.timber)


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug / Release Configurations
    // Compose preview, tooling
    debugImplementation(libs.bundles.compose.debug)
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)

    implementation(libs.kotlinx.serialization)


    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    implementation(project(":feature:tables:data"))
    implementation(project(":feature:tables:domain"))
    implementation(project(":feature:tables:ui"))

}