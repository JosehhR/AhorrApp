plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.ahorrapp"
    compileSdk = 36

    packagingOptions {
        resources.excludes.add("META-INF/androidx.cardview_cardview.version")
    }

    defaultConfig {
        applicationId = "com.example.ahorrapp"
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
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11" // <-- LÍNEA MODIFICADA
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.cardview.v7)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // BoM de Firebase para gestionar versiones
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Dependencia de Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // Dependencia de Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")
}