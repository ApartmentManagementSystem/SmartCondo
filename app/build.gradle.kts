plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.mit.apartmentmanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mit.apartmentmanagement"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("androidx.activity:activity:1.8.2")
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // GSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Secure storage
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Circle ImageView
    implementation(libs.circleimageview)

    // Websocket
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // STOMP
    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")

    // RxJava 2
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // ProcessBar
    implementation("com.github.ybq:Android-SpinKit:1.4.0")

    // Paging
    val pagingVersion = "3.3.2"
    implementation("androidx.paging:paging-runtime:$pagingVersion")
    testImplementation("androidx.paging:paging-common:$pagingVersion")
    implementation("androidx.paging:paging-rxjava2:$pagingVersion")
    implementation("androidx.paging:paging-rxjava3:$pagingVersion")
    implementation("androidx.paging:paging-guava:$pagingVersion")
    implementation("androidx.paging:paging-compose:3.3.2")

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
    }
}