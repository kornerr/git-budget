plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    macosX64("native") {
        binaries {
            sharedLib {
                baseName = "gb"
            }
        }
    }
}

tasks.withType<Wrapper> {
    //gradleVersion = "8.12"
    distributionType = Wrapper.DistributionType.ALL
}
