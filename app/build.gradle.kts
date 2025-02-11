plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.mikepenz.aboutlibraries.plugin")
    id("org.jetbrains.kotlin.plugin.compose")
}
android {
    namespace = "com.mukund.bookcompanion"
    compileSdk = 34

    defaultConfig {
        applicationId="com.mukund.bookcompanion"
        minSdk=29
        targetSdk=34
        versionCode=26
        versionName="0.1.5"

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
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
        androidResources {
        generateLocaleConfig = true
    }
}

dependencies {
    // AppCompat
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.appcompat:appcompat-resources:1.7.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Compose
    implementation("androidx.compose.material3:material3:1.3.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")

    // AboutLibraries
    implementation("com.mikepenz:aboutlibraries-core:11.2.3")
    releaseImplementation("com.mikepenz:aboutlibraries-compose:11.2.3")

    // backup
    implementation("com.google.code.gson:gson:2.11.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Others
    implementation("androidx.activity:activity-ktx:1.9.3")
}