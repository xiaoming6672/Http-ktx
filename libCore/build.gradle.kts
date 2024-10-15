import com.android.build.gradle.internal.api.LibraryVariantOutputImpl

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

apply(from = rootProject.file("AliMaven.gradle.kts"))

val libGroupId = "com.lib.xiaoming"
val libArtifactId = "http-ktx"
val libVersionCode = 2
val libVersionName = "0.1.1"


android {
    namespace = "com.zhang.lib.httpktx"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt") , "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    android.libraryVariants.all {
        outputs.all {
            if (this is LibraryVariantOutputImpl) {
                outputFileName = "${rootProject.name}-$name-$libVersionCode-$libVersionName.aar"
            }
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.startUp)

    api(libs.gson)
    api(libs.retrofit.core)
    api(libs.retrofit.converter.gson)
    api(libs.okhttp3.logging.interceptor)

    implementation(libs.library.utils)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = libGroupId
            artifactId = libArtifactId
            version = libVersionName

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            setUrl(extra["aliMavenUrl"].toString())
            credentials {
                username = extra["aliMavenName"].toString()
                password = extra["aliMavenPassword"].toString()
            }
        }
    }
}