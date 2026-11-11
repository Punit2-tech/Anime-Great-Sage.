
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.projectraphael.animegreatsage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.projectraphael.animegreatsage"
        minSdk = 24
        targetSdk = 35
        versionCode = 14
        versionName =  "1.3.4"
    }

    buildFeatures {
        compose = true
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
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:35.1.0"))
    implementation("com.google.firebase:firebase-ai")
    debugImplementation("com.google.firebase:firebase-appcheck-debug")

    // Existing Android dependencies
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
