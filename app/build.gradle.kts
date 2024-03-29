plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "flashlight.phichung.com.torch"
    compileSdk = 34

    defaultConfig {
        applicationId = "flashlight.phichung.com.torch"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue ("string", "str_ads_google_app_id", "ca-app-pub-4824353242373249~8219707070")
            resValue ("string", "str_ads_banner", "ca-app-pub-4824353242373249/4433000143")
            resValue ("string", "str_ads_open", "ca-app-pub-4824353242373249/1921781637")
        }
        debug {
            isMinifyEnabled = false
            resValue ("string", "str_ads_google_app_id", "ca-app-pub-3940256099942544~3347511713")
            resValue ("string", "str_ads_banner", "ca-app-pub-3940256099942544/6300978111")
            resValue ("string", "str_ads_open", "ca-app-pub-3940256099942544/9257395921")
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
        compose = true
        buildConfig = true

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}

val billingLibraryVersion = rootProject.extra.get("billingLibraryVersion") as String
val appcompatX = rootProject.extra.get("appcompatX") as String

dependencies {
    implementation("com.android.billingclient:billing:$billingLibraryVersion")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:$appcompatX")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //splashscreen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    //navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    //color picker
    implementation ("com.github.QuadFlask:colorpicker:0.0.15")

    //Gson
    implementation("com.google.code.gson:gson:2.10.1")

    //lib for camera
    implementation(project(":camposer"))

    //lib for billing
    implementation(project(":billing-utils"))

    //lib for language picker
    implementation(project(":languagepicker"))

    // coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-video:2.5.0")



    //request permission
    implementation ("com.google.accompanist:accompanist-permissions:0.34.0")

    // admob
    implementation("com.google.android.gms:play-services-ads:23.0.0")

    val lifecycle_version = "2.6.2"


    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    kapt ("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    // For apps targeting Android 12, add WorkManager dependency.
    implementation("androidx.work:work-runtime:2.9.0")

}