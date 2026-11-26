// swift-tools-version: 5.9

import PackageDescription

let package = Package(
  name: "gb",
  products: [
    .library(
      name: "gb",
      targets: ["gb"]
    ),
  ],
  targets: [    
    .binaryTarget(name: "gb", path: "gb.xcframework.zip")
  ]
)
