plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}




android {
    namespace = "com.haohao.billing"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.android.billingclient:billing:6.2.0")

}
