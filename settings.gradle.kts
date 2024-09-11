pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

//    resolutionStrategy {
//        eachPlugin {
//            if (requested.id.id == "com.android.application" || requested.id.id == "com.android.library") {
//                useVersion("8.5.0")
//            }
//        }
//    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LocalAPI"
include(":app")