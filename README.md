Git Budget is a cross-platform application with shared logic driven
by [Kotlin Dialect](https://github.com/OGStudio/kotlin-dialect)

## Supported platforms

| Platform | Status |
| --- | --- |
| Android | √ |
| iOS | √ |
| Linux | Pending |
| macOS | √ |
| Windows | Pending |

### Android 

https://github.com/user-attachments/assets/56cca84f-23f6-49d8-b30d-560fbb3de874

### iOS

https://github.com/user-attachments/assets/d7226d0e-739d-45d8-9bd8-d8ba18d0aea6

### macOS

https://github.com/user-attachments/assets/9053b76d-43d0-4d91-b03f-9f88b723ad1d

## Project structure

| Path | Role |
|------|------|
| `kd.yml` | Kotlin Dialect schema: `BudgetContext` and output paths for generated code |
| `util/` | Build helper scripts |
| `ver-android/` | Android application |
| `ver-ios/` | iOS application |
| `sdk-ios/` | Kotlin Multiplatform library for iOS |
| `sdk-mac-x64/` | Kotlin Multiplatform library for macOS |
| `ver-mac-x64/` | macOS application |

## Statistics

Shared logic is in Kotlin Multiplatform. So, how many lines of code (LOC)
were saved (i.e. not duplicated) for each platform?

| Platform |  Saved LOC | Saved % | Total LOC |
| --- | --- | --- | --- |
| `ver-android` | 0 (Original Kotlin code) | 0% | 652 |
| `ver-ios` + `sdk-ios` | 333 | 58% | 569 |
| `ver-mac-x64` + `sdk-mac-x64` | 340 | 44% | 761 |

## Build dependencies

### All platforms

* Git: clone Kotlin Dialect's `Klin` code generator
* Node.js: run `Klin` code generator

### Android

* Android Studio Panda 2 (2025.3.2)

### iOS

* Xcode 26
* XcodeGen
* OpenJDK 21

### macOS Intel

* CMake
* Qt 6
* OpenJDK 21
