// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "hello-platform",
    platforms: [
        .macOS(.v13)
    ],
    targets: [
        .executableTarget(
            name: "hello-platform",
            path: "src"
        )
    ]
)
