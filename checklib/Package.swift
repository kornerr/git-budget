// swift-tools-version:6.2
import PackageDescription

let package = Package(
    name: "Checklib",
    platforms: [
       .macOS(.v10_15)
    ],
    products: [
        .library(name: "Checklib", type: .dynamic, targets: ["Checklib"]),
    ],
    dependencies: [
        .package(url: "https://github.com/swifdroid/jni-kit.git", from: "2.0.0"),
        .package(url: "https://github.com/swifdroid/AndroidLogging.git", from: "0.1.0"),
        .package(url: "https://github.com/apple/swift-log.git", from: "1.6.2"),
    ],
    targets: [
        .target(
            name: "Checklib",
            dependencies: [
                .product(name: "JNIKit", package: "jni-kit"),
                .product(name: "Logging", package: "swift-log"),
                .product(name: "AndroidLogging", package: "AndroidLogging", condition: .when(platforms: [.android])),
            ]
        ),
    ])
