plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.berserklifecounter" // Изменено имя namespace
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.berserklifecounter" // Изменено applicationId
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.compose.material3:material3:1.2.0") // Используйте последнюю версию
    implementation("androidx.compose.ui:ui:1.5.1") // Или последнюю версию, согласованную с BOM
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // Или последнюю версию
    implementation("androidx.activity:activity-compose:1.8.2") // Или последнюю версию
    implementation(platform("androidx.compose:compose-bom:2023.03.00")) // Или последнюю версию BOM
    implementation("androidx.compose.animation:animation:1.5.1") // Или последнюю версию, согласованную с BOM
    implementation("androidx.compose.animation:animation-core:1.5.1") // Или последнюю версию, согласованную с BOM
    implementation("io.coil-kt:coil-compose:2.4.0") // Одна строка
    implementation("androidx.navigation:navigation-compose:2.7.5") // Или последнюю версию
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}