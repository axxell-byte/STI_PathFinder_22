plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.stipathfinder"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.stipathfinder"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Godot 3.5 version for placeholder
        manifestPlaceholders.put("godotEditorVersion", "3.5")
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(project(":godot"))


    // Use latest MSAL version and exclude the dual-screen transitive dependency
    implementation("com.microsoft.identity.client:msal:5.1.0") {
        exclude(group = "com.microsoft.device.display", module = "display-mask")
    }

    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Optional: globally exclude display-mask if pulled in by other dependencies
configurations.all {
    exclude(group = "com.microsoft.device.display", module = "display-mask")
}
