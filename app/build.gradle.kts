/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)

    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
    id("androidx.room")
    alias(libs.plugins.ksp)
}

android {

    namespace = "com.zavedahmad.yaHabit"
    compileSdk = 36

    dependenciesInfo {
        // Disables dependency metadata when building APKs (for IzzyOnDroid/F-Droid)
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles (for Google Play)
        includeInBundle = false
    }


    defaultConfig {
        applicationId = "com.zavedahmad.yaHabit"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 36
        versionCode = 17
        versionName = "0.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {

            isMinifyEnabled =true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
room{
    schemaDirectory("${projectDir}/src/main/java/com/zavedahmad/yaHabit/roomDatabase/schemas")
}

dependencies {

        implementation( "androidx.core:core-splashscreen:1.0.0")

    // glance for widgets
//    implementation("androidx.glance:glance-appwidget:1.1.1")
    // For interop APIs with Material 3
    implementation("androidx.glance:glance-material3:1.1.1")
    // end

    implementation("io.github.ehsannarmani:compose-charts:0.1.7")

    implementation(libs.reorderable)
    //calander
    implementation("com.kizitonwose.calendar:compose:2.7.0")
    //room imports
    val roomVersion = "2.7.1"

    implementation("androidx.room:room-runtime:$roomVersion")

    //Material kolor -- custom colors

    implementation("com.materialkolor:material-kolor:3.0.0-beta07")
    // end
    ksp("androidx.room:room-compiler:$roomVersion")


    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    //end
//    retrofit imports

//    end
//    coil install
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")
//    end
//    shimmer dependency
    implementation("com.valentinilk.shimmer:compose-shimmer:1.3.3")
//    end
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.windowsizeclass)
    implementation(libs.androidx.adaptive.layout)
//    implementation(libs.androidx.material3.navigation3)


    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}