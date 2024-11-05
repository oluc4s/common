plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("maven-publish")
}

android {
    namespace = "com.s2start.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material)

    //Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)


    //Coil
    implementation(libs.coil.compose)
    implementation("io.insert-koin:koin-core:3.3.2")
    implementation("io.insert-koin:koin-android:3.3.2")
    implementation("io.insert-koin:koin-androidx-compose:3.4.1")


    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation("com.airbnb.android:lottie-compose:6.4.0")
    implementation("com.google.android.exoplayer:exoplayer:2.15.1")
    implementation("com.valentinilk.shimmer:compose-shimmer:1.3.0")
}


publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.oluc4s"
            artifactId = "core"
            version = "1.3"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}