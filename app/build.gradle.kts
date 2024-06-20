import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.c241ps319.patera"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.c241ps319.patera"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //load the values from .properties file
        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //return empty key in case something goes wrong
        val baseUrl1 = properties.getProperty("BASE_URL1")
        val baseUrl2 = properties.getProperty("BASE_URL2")

        buildConfigField("String", "BASE_URL1", baseUrl1)
        buildConfigField("String", "BASE_URL2", baseUrl2)
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
        buildConfig = true
        viewBinding = true
        mlModelBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.exifinterface)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    Lifecycle KTX
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

//    circle Image
    implementation(libs.circleimageview)

//    Retrofit
    implementation(libs.retrofit2.converter.gson)

    //u crop
    implementation(libs.ucrop)

//    For Onboarding
    implementation(libs.dotsindicator)
    implementation(libs.androidx.viewpager2)

    //    TF
    implementation(libs.tensorflow.lite.task.vision)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)


    //    Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.lifecycle.runtime.ktx) // untuk lifecycleScope

    //    datastore
    implementation(libs.androidx.datastore.preferences)

    //    coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Glide
    implementation(libs.glide)
    
    implementation(kotlin("script-runtime"))

    //shimmer
    implementation(libs.shimmer)
}