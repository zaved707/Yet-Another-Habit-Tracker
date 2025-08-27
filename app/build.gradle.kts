
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)



    id("kotlin-parcelize")
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
        versionCode = 22
        versionName = "0.3.1"

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


dependencies {
    //room
    implementation(libs.androidx.room.runtime)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)

   

    implementation("io.github.ehsannarmani:compose-charts:0.1.7")

    implementation(libs.reorderable)
    //calander
    implementation("com.kizitonwose.calendar:compose:2.7.0")



    //Material kolor -- custom colors

    implementation("com.materialkolor:material-kolor:3.0.0-beta07")
    // end




    //end
//    retrofit imports

//    end
//    coil install

//    end
//    shimmer dependency

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

    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material.icons.extended)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation( "androidx.core:core-splashscreen:1.0.0")
    implementation(project(":database"))
    implementation(project(":common"))
    implementation(project(":widgets"))
    implementation(libs.material.icons.extended)

    // koin
    val koin_version = "4.0.3"

    implementation("io.insert-koin:koin-androidx-compose:${koin_version}")
    implementation("io.insert-koin:koin-androidx-compose-navigation:${koin_version}")





}