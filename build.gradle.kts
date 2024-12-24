plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}



allprojects {
    // Remove repository definitions here, as they're already handled in settings.gradle.kts
}

tasks.register<Delete>("clean") {
    delete(project.buildDir)  // Corrected usage to avoid deprecated warning
}
