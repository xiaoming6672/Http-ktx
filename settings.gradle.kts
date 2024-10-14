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

apply(from = "AliMaven.gradle.kts")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://maven.aliyun.com/repository/apache-snapshots")

        maven(url = "https://jitpack.io")
        maven(url = "https://maven.aliyun.com/repository/public")


        maven {
            setUrl(extra["aliMavenUrl"].toString())
            credentials {
                username = extra["aliMavenName"].toString()
                password = extra["aliMavenPassword"].toString()
            }
        }

        google()
        mavenCentral()
    }
}

rootProject.name = "Http-ktx"
include(":app")
include(":libCore")
