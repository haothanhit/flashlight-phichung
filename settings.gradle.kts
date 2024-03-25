pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven (url="https://jitpack.io")

    }
}

rootProject.name = "Flashlight"
include(":app")
include(":camposer")
include(":languagepicker")
include(":billing-utils")
