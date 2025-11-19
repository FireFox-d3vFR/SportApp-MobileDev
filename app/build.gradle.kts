plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.sportapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.sportapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        buildConfigField(
//            "String",
//            "GUARDIAN_API_KEY",
//            "\"17a3dd53-4583-4e91-a086-450013a7dba5\""
//        )
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

    configurations.all {
        exclude(group = "com.intellij", module = "annotations")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.work)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.retrofit)
    implementation(libs.coverter.moshi)
    implementation(libs.loggin.interceptor)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    ksp(libs.moshi.kapt)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.compiler)

    ksp(libs.room.compiler)

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.svg)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}