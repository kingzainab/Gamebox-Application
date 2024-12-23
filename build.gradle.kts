plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    val kotlinVersion = "1.8.10" // Kotlin version updated for compatibility
    dependencies {
        classpath(libs.gradle) // Gradle plugin version
        classpath(libs.kotlin.gradle.plugin)
    }
}

allprojects {
    // Remove repository definitions here, as they're already handled in settings.gradle.kts
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
