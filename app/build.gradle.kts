import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.9.0"

    kotlin("kapt")
}

android {
    namespace = "com.example.safebit"
    compileSdk = 34
    val file = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(FileInputStream(file))
    defaultConfig {
        applicationId = "com.example.safebit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_URL", properties.getProperty("API_URL"))
        buildConfigField("String", "API_PASSWORD", properties.getProperty("API_PASSWORD"))

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

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    implementation(libs.ktor.client.cio)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform("io.github.jan-tennert.supabase:bom:2.6.1"))
    implementation ("io.github.jan-tennert.supabase:postgrest-kt:2.6.1-dev")
    implementation("io.ktor:ktor-client-android:2.3.12")
    implementation ("io.github.cdimascio:java-dotenv:5.2.2")
}