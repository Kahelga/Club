plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.club"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.club"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

 /*       testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }*/
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":util:formatting"))
    implementation(project(":util:validation"))
    implementation(project(":util:manager:token"))
    implementation(project(":design:resources"))
    implementation(project(":shared:user:auth"))
    implementation(project(":shared:user:profile"))
    implementation(project(":shared:event"))
    implementation(project(":shared:tickets"))
    implementation(project(":shared:report"))
    implementation(project(":feature:poster"))
    implementation(project(":feature:eventDetails"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:registration"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:profileUpdate"))
    implementation(project(":feature:purchase"))
    implementation(project(":feature:booking"))
    implementation(project(":feature:tickets"))
    implementation(project(":feature:hall"))
    implementation(project(":feature:admin:events"))
    implementation(project(":feature:admin:reports"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.coil.kt.coil.compose)

    // Навигация с Compose
    implementation(libs.androidx.navigation.compose)

    // Работа с сетью
    implementation(libs.retrofit);
    implementation(libs.retrofit2.kotlinx.serialization.converter);
    implementation(libs.logging.interceptor)

    //MockWebServer
    implementation (libs.okhttp)
    implementation(libs.mockwebserver.v4110)
    implementation(libs.androidx.monitor)
    testImplementation(libs.mockwebserver)

    // Сериализация JSON
    implementation(libs.kotlinx.serialization.json);

    // Асинхронное программирование
    implementation(libs.kotlinx.coroutines.android)
    //безопасность
    implementation(libs.androidx.security.crypto)

    implementation(libs.compose)
    implementation (libs.androidx.material.icons.extended)
    testImplementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}