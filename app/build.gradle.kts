plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.notify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notify"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packagingOptions {
        exclude( "META-INF/*")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.compose.foundation:foundation-android:1.6.1")
    implementation("androidx.compose.material3:material3-android:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation ("io.mockk:mockk:1.12.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0")
    implementation("androidx.activity:activity-compose:1.3.0-alpha07")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")
    implementation("androidx.compose.material3:material3-adaptive:1.0.0-alpha06")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha03")
    implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:20.4.0")
    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.3")
    implementation("com.joanzapata.pdfview:android-pdfview:1.0.4@aar")
    implementation("com.google.cloud:google-cloud-vision:2.0.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:0.25.2")

    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation ("com.github.barteksc:pdfium-android:1.9.0")
    implementation ("io.grpc:grpc-okhttp:1.40.1")
    implementation ("io.grpc:grpc-stub:1.40.1")
    implementation ("javax.annotation:javax.annotation-api:1.3.2")

}

kapt {
    correctErrorTypes=true
}