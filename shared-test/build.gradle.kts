plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.myapplication.shared.test"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":app"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.junit4)
    implementation(libs.androidx.test.core.ktx)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.core)
    implementation(libs.hilt.android.testing)
    implementation(libs.junit.jupiter.api)
    kapt(libs.hilt.compiler)
    // kotest framework
    implementation(libs.kotest.runner.junit5)
    implementation(libs.kotest.assertions.core)
    implementation(libs.kotest.property)
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

}