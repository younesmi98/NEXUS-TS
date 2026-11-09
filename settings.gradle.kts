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
        maven("https://jitpack.io")
    }
}

rootProject.name = "NEXUS-TS"
include(":app", ":protocol", ":ts3j")
project(":protocol").projectDir = file("upstream/ts3x-client/protocol")
project(":ts3j").projectDir = file("upstream/ts3x-client/ts3j")
