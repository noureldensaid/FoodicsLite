pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Foodics Lite"
include(":app")
include(":core:common")
include(":core:ui")
include(":feature:tables:ui")
include(":feature:menu:ui")
include(":feature:orders:ui")
include(":feature:settings:ui")
include(":core:navigation")
include(":core:network")
include(":core:database")
include(":feature:tables:data")
include(":feature:tables:domain")
