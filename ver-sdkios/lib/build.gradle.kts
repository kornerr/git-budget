import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    //alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
        }
    }
}

kotlin {
    androidTarget {
    }
}

android {
    namespace = "org.opengamestudio"
    compileSdk = 35
}

kotlin {
    val xcf = XCFramework()
    arrayOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64(),
    ).forEach {
        it.binaries.framework {
            baseName = "sdkios"
            xcf.add(this)
            isStatic = false
        }
    }
}
