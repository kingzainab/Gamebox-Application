// pluginManagement {
//     includeBuild("build-logic") // Remove this line if not needed
//     repositories {
//         gradlePluginPortal()
//         google()
//         mavenCentral()
//     }
// }
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Game Box"
include(":app")