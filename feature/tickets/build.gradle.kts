plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.club.feature.tickets"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":shared:tickets"))
    implementation(project(":shared:event"))
    implementation(project(":shared:user:auth"))
    implementation(project(":util:manager:token"))
    implementation(project(":feature:auth"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.coil.kt.coil.compose)

    // Работа с сетью
    implementation(libs.retrofit);
    implementation(libs.retrofit2.kotlinx.serialization.converter);
    implementation(libs.logging.interceptor)

    // Сериализация JSON
    implementation(libs.kotlinx.serialization.json);
    // Асинхронное программирование
    implementation(libs.kotlinx.coroutines.android)
    // Навигация с Compose
    implementation(libs.androidx.navigation.compose)
    //безопасность
    implementation(libs.androidx.security.crypto)

    //qr код
    implementation(libs.core)
    implementation(libs.zxing.android.embedded)

    implementation(libs.compose)
    implementation (libs.androidx.material.icons.extended)

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":util:formatting"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}