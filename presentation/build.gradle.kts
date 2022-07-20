plugins {
    id ("com.android.library")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = SdkVersions.compileSdk

    defaultConfig {
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isTestCoverageEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
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
    implementation (fileTree (mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation (project (":domain"))
    implementation (project (":data"))

    implementation (KTX.CORE)
    implementation (AndroidX.APP_COMPAT)
    implementation (Google.MATERIAL)
    implementation (AndroidX.CONSTRAINT_LAYOUT)
    implementation (AndroidX.LEGACY)

    androidTestImplementation (TestTool.EXT_JUNIT)
    androidTestImplementation (TestTool.ANDROID_JUNIT)
    androidTestImplementation (TestTool.ANDROID_ESPRESSO)

    // dagger hilt
    implementation (DaggerHilt.DAGGER_HILT)
    kapt (DaggerHilt.DAGGER_HILT_COMPILER)
    implementation (DaggerHilt.DAGGER_HILT_VIEW_MODEL)
    kapt (DaggerHilt.DAGGER_HILT_ANDROIDX_COMPILER)

    // ViewModel
    implementation (AndroidX.LIFECYCLE_VIEW_MODEL)

    // LiveData
    implementation (AndroidX.LIFECYCLE_LIVEDATA)

    // Retrofit
    implementation (Retrofit.RETROFIT)
    implementation (Retrofit.CONVERTER_GSON)
    implementation (Retrofit.CONVERTER_JAXB)

    //okHttp
    implementation (OkHttp.OKHTTP)
    implementation (OkHttp.LOGGING_INTERCEPTOR)

    //coroutines
    implementation (Coroutines.COROUTINES)

    //by viewModel
    implementation (AndroidX.ACTIVITY)
    implementation (AndroidX.FRAGMENT)

    //CameraX
    implementation (CameraX.CAMERA_CORE)
    implementation (CameraX.CAMERA_CAMERA2)
    implementation (CameraX.CAMERA_LIFECYCLE)
    implementation (CameraX.CAMERA_VIEW)
    implementation (CameraX.CAMERA_EXTENSIONS)

    //seekbar
    implementation (UI.CHRISVIN_RUBBER_PICKER)

    //nav component
    implementation (NavComponent.NAVIGATION_FRAGMENT)
    implementation (NavComponent.NAVIGATION_UI)
    implementation (NavComponent.NAVIGATION_DYNAMIC_FEATURES_FRAGMENT)
    androidTestImplementation (NavComponent.NAVIGATION_TESTING)
    implementation (NavComponent.NAVIGATION_COMPOSE)

    //lottie 애니메이션
    implementation (UI.LOTTIE)

    //pytorch
    implementation (Pytorch.LITE)
    implementation (Pytorch.TORCH_VISION)

    //datastore
    implementation (AndroidX.DATASTORE)
}