plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.login"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.login"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // Dùng getByName để cấu hình một build type đã có (như "release")
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // ⭐ SỬA LỖI 1: Cú pháp .kts dùng dấu '=' và 'isCoreLibraryDesugaringEnabled'
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true // Sửa từ 'coreLibraryDesugaringEnabled'
    }
}

dependencies {
    implementation("com.google.android.material:material:1.11.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // Retrofit for REST API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter GSON for automatic JSON <-> Java object mapping
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // ⭐ SỬA LỖI 2: Cú pháp .kts dùng dấu ngoặc đơn ()
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}