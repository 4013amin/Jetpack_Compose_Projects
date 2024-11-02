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
        mavenCentral()
        google()
        maven {
            url = uri("https://webrtc.github.io/repo/m2") // WebRTC repository
        }
    }
}

rootProject.name = "JetPackShop"
include(":app")
