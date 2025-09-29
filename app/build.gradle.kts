plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.aboutLibraries)
}
android {
    namespace = "com.mukund.bookcompanion"
    compileSdk = 36

    defaultConfig {
        applicationId="com.mukund.bookcompanion"
        minSdk=29
        targetSdk=36
        versionCode=27
        versionName="0.2.0"

        vectorDrawables {
            useSupportLibrary = true
        }
        versionNameSuffix = ""
    }


    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
        androidResources {
        generateLocaleConfig = true
    }
    buildToolsVersion = "36.0.0"
}

dependencies {
    // AppCompat
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat.resources)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.ui.ktx)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.ksp)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.test)

    // Material 3
    implementation(libs.androidx.material3)

    // Compose
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime)

    // AboutLibraries
    implementation(libs.aboutlibraries.core)
    releaseImplementation(libs.aboutlibraries.compose.core)

    // backup
    implementation(libs.gson)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Others
    implementation(libs.androidx.activity.ktx)
    implementation(libs.accompanist.systemuicontroller)
}