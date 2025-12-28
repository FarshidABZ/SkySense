import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.buildKonfig)
    alias { libs.plugins.mokkery }
}

kotlin {
    androidLibrary {
        namespace = "com.farshidabz.skysense"
        compileSdk = 36
        minSdk = 26
        withHostTest { }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines.core)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            // Lifecycle & Room
            api(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            // Dependency Injection
            implementation(libs.koin.core)

            implementation(libs.kermit)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.ktor.test)
            // Coroutines test for runTest
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }
    }
}

buildkonfig {
    packageName = "com.farshidabz.skysense"
    objectName = "AppConfig"

    defaultConfigs {
        buildConfigField(STRING, "API_KEY", getLocalProperty("WEATHER_API_KEY"))
        buildConfigField(STRING, "BASE_URL", "https://api.weatherapi.com/v1/")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // KSP for Room (Android only)
    add("kspAndroid", libs.androidx.room.compiler)
}

// Utility function for reading local properties
fun getLocalProperty(key: String): String {
    val localProperties = rootProject.file("local.properties")
    if (!localProperties.exists()) {
        return System.getenv(key).orEmpty()
    }

    return localProperties.useLines { lines ->
        lines.firstOrNull { it.startsWith("$key=") }
            ?.substringAfter("=")
    } ?: System.getenv(key).orEmpty()
}