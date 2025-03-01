plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "app.tauri.cloud_storage"
    compileSdk = 34

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

    // resources {
    //excludes += "META-INF/INDEX.LIST"
    //}

    // packaging { resources.excludes.add("META-INF/*") }

    // packaging {
    //     resources.excludes.add("META-INF/*")
    //     // jniLibs.excludes.add("META-INF/*")
    // }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("com.google.android.material:material:1.7.0")
    // https://developer.android.com/identity/sign-in/credential-manager#add-dependencies
    // https://developer.android.com/jetpack/androidx/releases/credentials
    //implementation("androidx.credentials:credentials:1.5.0-rc01")
    implementation("androidx.credentials:credentials:1.3.0")
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    // implementation("androidx.credentials:credentials-play-services-auth:1.5.0-rc01")

    // The BOM will manage the module versions and transitive dependencies
    // implementation(platform("com.google.auth:google-auth-library-bom:1.30.1"))
    // Replace with the module(s) that are needed
    // implementation("com.google.auth:google-auth-library-oauth2-http")
    // https://mvnrepository.com/artifact/com.google.api-client/google-api-client
    // implementation("com.google.api-client:google-api-client:2.7.1")
    // https://search.maven.org/search?q=a:google-api-services-drive
    implementation("com.google.apis:google-api-services-drive:v3-rev20250122-2.0.0")
    implementation("com.google.android.gms:play-services-basement:18.5.0")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(project(":tauri-android"))
}
