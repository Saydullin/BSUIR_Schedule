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

rootProject.name = "iis"
include(":app")
include(":feature:groups")
include(":feature:employees")
include(":domain")
include(":data")
include(":core")
include(":core:database")
include(":core:resource")
include(":ui-kit")
include(":feature:departments")
include(":core:retrofit")
