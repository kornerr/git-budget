plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "org.opengamestudio.checklib"
    compileSdk = 35

    defaultConfig {
        minSdk = 28
        consumerProguardFiles("consumer-rules.pro")
    }

    packaging {
        jniLibs {
            keepDebugSymbols.add("*/arm64-v8a/*.so")
            keepDebugSymbols.add("*/armeabi-v7a/*.so")
            keepDebugSymbols.add("*/x86_64/*.so")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    sourceSets["main"].jniLibs.srcDirs("src/main/jniLibs")

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // managed by swiftstreamide: so-dependencies-begin
    implementation("com.github.swifdroid.runtime-libs:core:6.2.0-16kb")
    implementation("com.github.swifdroid.runtime-libs:foundation:6.2.0-16kb")
    implementation("com.github.swifdroid.runtime-libs:foundationessentials:6.2.0-16kb")
    implementation("com.github.swifdroid.runtime-libs:i18n:6.2.0-16kb")
    // managed by swiftstreamide: so-dependencies-end
}

allprojects {}
