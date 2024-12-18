plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.zsinnovations.gamebox"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zsinnovations.gamebox"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gson)
    implementation(libs.circleimageview)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.gridlayout)
    implementation(libs.core.ktx)
    implementation(libs.datastore.preferences) // Added dependency
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.org.jetbrains.kotlin.kotlin.stdlib)
    implementation (libs.core.ktx.v132)
    implementation (libs.appcompat.v120)
    implementation (libs.material.v130)
    implementation (libs.constraintlayout.v204)
    testImplementation (libs.junit)
    androidTestImplementation (libs.junit.v112)
    androidTestImplementation (libs.espresso.core.v330)

    // Datastore
    implementation (libs.datastore.preferences.v100alpha06)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation (libs.lifecycle.viewmodel.ktx)
    implementation (libs.lifecycle.runtime.ktx)

    // Room components
    implementation (libs.room.ktx)
    kapt (libs.androidx.room.compiler)
    androidTestImplementation (libs.androidx.room.testing)

    // Card View
    implementation (libs.androidx.cardview)

    //Lottie
    implementation (libs.lottie)

    //Konfetti
    implementation (libs.konfetti)
}
