import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

configure<ApplicationExtension> {
    namespace = "com.example.fieldsync_inventory_app"
    compileSdk = 36

    defaultConfig {
        applicationId = "gov.mofe.rcrc_seed_manager"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "gov.mofe.rcrc_seed_manager.HiltTestRunner"

        // Read BASE_URL from local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(FileInputStream(localPropertiesFile))
        }

        var baseUrl = localProperties.getProperty("BASE_URL")
        baseUrl = baseUrl.replace("\"", "") // Remove any quotes from local.properties
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildFeatures {
        buildConfig = true
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
        compose = true
    }

    // Compose options
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

dependencies {
    // Core Android and Kotlin libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM (Bill of Materials) to manage versions
    implementation(platform(libs.androidx.compose.bom))

    // Core Compose libraries
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.compose.material.icons.extended)

    // Compose Navigation for single-activity navigation
    implementation(libs.androidx.navigation.compose)

    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.foundation)
    ksp(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp (includes the interceptor capabilities)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // JWT Utility
    implementation(libs.java.jwt)

    // Room
    implementation(libs.androidx.room.runtime)
    // To use Kotlin Symbol Processing (KSP)
    ksp(libs.androidx.room.compiler)
    // To use Kotlin Coroutines for Room
    implementation(libs.androidx.room.ktx)

    // *** TEST DEPENDENCIES ***
    // For using Google's Truth assertion library in Android tests
    androidTestImplementation(libs.truth)

    // Dependencies for Mocking
    // Mockito is the mocking framework
    androidTestImplementation(libs.mockito.core)
    // mockito-inline is required to mock final classes and methods (which is needed for some Android classes)
    androidTestImplementation(libs.mockito.android)

    // 1. DataStore (Preferences is the simpler choice for a single JWT)
    implementation(libs.androidx.datastore.preferences.core) // Or latest stable
    implementation(libs.androidx.datastore.preferences)

    // 2. Google Tink for cryptography/key management
    implementation(libs.tink.android) // Or latest stable

    // Hilt testing
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    // !!! IMPORTANT: ADD THIS LINE FOR LOCAL UNIT TESTS !!!
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)

    // For Room database testing with in-memory database
    androidTestImplementation(libs.androidx.room.testing) // Use the same version as your Room library
    // For testing coroutines, providing runTest and other utilities
    androidTestImplementation(libs.kotlinx.coroutines.test) // Check for the latest stable version
    // For testing LiveData and other Architecture Components, including InstantTaskExecutorRule
    androidTestImplementation(libs.androidx.core.testing) // Check for the latest stable version


    // Unit tests
    testImplementation(libs.junit)

    // Android-specific tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging tools for Compose
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Excel
    implementation(libs.poi)
    implementation(libs.poi.ooxml)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)
}