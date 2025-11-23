plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.univalle.inventoryapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.univalle.inventoryapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        dataBinding = true
    }

}

dependencies {
    val navVersion = "2.3.5"
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    implementation("androidx.navigation:navigation-fragment-ktx:${navVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${navVersion}")
    implementation("androidx.navigation:navigation-common:${navVersion}")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.activity:activity-ktx:1.8.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
    implementation ("com.getbase:floatingactionbutton:1.10.1")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")

    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha05")

    implementation("com.airbnb.android:lottie:6.1.0")

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}

kotlin {
    jvmToolchain(17)
}