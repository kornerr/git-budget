// swift-tools-version: 5.10
import Foundation
import PackageDescription

let resDir = ProcessInfo.processInfo.environment["APP_RES_DIR"]!

let package = Package(
    name: "hello-platform",
    dependencies: [
        .package(url: "https://github.com/moreSwift/swift-cross-ui", exact: "0.10.0")
    ],
    targets: [
        .executableTarget(
            name: "hello-platform",
            dependencies: [
                .product(name: "SwiftCrossUI", package: "swift-cross-ui"),
                .product(name: "WinUIBackend", package: "swift-cross-ui")
            ],
            path: "src",
            linkerSettings: [
                .unsafeFlags(["-Xlinker", "\(resDir)/AppIcon.res"])
            ]
        )
    ]
)
