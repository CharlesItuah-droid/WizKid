// build.gradle.kts (:app)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler) // Corrected typical alias for Kotlin Compose Compiler plugin
    alias(libs.plugins.google.ksp) // IMPORTANT: Add KSP plugin alias
}

android {
    namespace = "com.example.wizkid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.wizkid"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    // composeOptions for Kotlin Compose Compiler plugin (if not handled by plugin defaults)
    // Often, the plugin handles this, but if you need to specify the version:
    // composeOptions {
    //     kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    // }
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Depends on room-runtime

    // HYPOTHETICAL: Assuming 'libs.retrofit' was identified as the source of 'com.intellij:annotations:12.0'
    // You MUST replace this with the actual library identified by the dependency tree.
    implementation(libs.retrofit) {
        exclude(group = "com.intellij", module = "annotations")
    }
    // --- End of Hypothetical Exclusion Example ---

    // Ensure the desired version of org.jetbrains:annotations is available.
    // It's often brought in by Kotlin itself or other modern libraries.
    // To be explicit (ensure you have this alias in libs.versions.toml for e.g. "23.0.0"):
    implementation(libs.jetbrains.annotations)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.converter.gson) // For Retrofit

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room Compiler using KSP
    ksp(libs.androidx.room.compiler)
}

// Global Resolution Strategy (Fallback if targeted exclusion is difficult or if KSP itself is the root issue)
// Uncomment this section if the targeted exclusion above doesn't work or isn't feasible.
/*
configurations.all {
    resolutionStrategy {
        // Ensure libs.jetbrains.annotations correctly resolves to "org.jetbrains:annotations:23.0.0" or newer
        force(libs.jetbrains.annotations)
        // Or directly:
        // force("org.jetbrains:annotations:23.0.0")
    }
}
*/