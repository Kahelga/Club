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

rootProject.name = "Club"
include(":app")
include(":design:resources")
include(":feature:poster")
include(":shared:event")
include(":feature:eventDetails")
include(":shared:user:auth")
include(":feature:auth")
include(":feature:registration")
include(":shared:user:profile")
include(":feature:profile")
include(":feature:profileUpdate")
include(":shared:tickets")
include(":feature:purchase")
include(":feature:tickets")
include(":feature:hall")
include(":util:formatting")
include(":util:manager:token")
include(":util:validation")
include(":shared:report")
include(":feature:admin:events")
include(":feature:admin:reports")
