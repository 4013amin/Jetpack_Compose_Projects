// settings.gradle
pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // این مخزن شامل aapt2 است.
        mavenCentral()
    }
}


rootProject.name = "JetPackShop"
include(":app")
