# git-budget

Multi-platform budget app: **Android** (Jetpack Compose), **iOS** (SwiftUI with a Kotlin Multiplatform framework), and **macOS** (Qt 6 + QML with a Kotlin/Native `gb` SDK). Shared UI logic is driven by [Kotlin Dialect (KD)](https://github.com/OGStudio/kotlin-dialect) using `kd.yml` at the repo root.

## Project structure

| Path | Role |
|------|------|
| `kd.yml` | KD schema: `BudgetContext`, `NetRequest` / `NetResponse`, and output paths for generated code. |
| `util/` | Build helper scripts. `util/other` defines `~/dev-cache/kd` as the clone location for the KD compiler. |
| `ver-android/` | Android app (Gradle, Compose). Consumes generated `ignore.kd.*` under `app/.../org/opengamestudio/`. |
| `ver-ios/` | iOS app sources (`src/`). Xcode project is generated in `ver-ios/app/` (see [Run on iOS](#run-on-ios)). |
| `ver-ios/app/` | iOS app metadata: `project.yml` (XcodeGen), `Info.plist`, and `local/gb/` Swift package wrapping the `gb` XCFramework. |
| `sdk-ios/` | Kotlin Multiplatform `gb` library: Android + iOS targets, produces an **XCFramework** for the iOS app. |
| `sdk-mac-x64/` | Kotlin/Native `gb` shared library (`libgb.dylib`, **macosX64**) for the desktop app. |
| `ver-mac-x64/` | Qt 6 **CMake** project: links the macOS `gb` SDK and builds the `gbM64` binary. |

## Statistics

**Saved Kotlin LOC** is the Kotlin in `sdk-ios/gb` or `sdk-mac-x64/gb` that you would otherwise reimplement in the host stack (Swift, C++/QML). The Android app has no separate `gb` module here—the canonical shared Kotlin under `ver-android/.../kotlin` is the app—so that row is **0** for saved lines. **Total LOC** is the per-platform hand-written sources for that app plus the paired SDK: Android (`*.kt` under `ver-android/app/src/.../kotlin`); iOS + SDK (`ver-ios/src` Swift and storyboard, plus all `*.kt` under `sdk-ios/gb/src`); macOS + SDK (`ver-mac-x64/src` `*.cpp` / `*.h` / `*.qml`, plus all `*.kt` under `sdk-mac-x64/gb/src`). **Saved (%)** is `saved / total * 100`. Counts with `wc -l` (2026-04-27); symlinks in SDK trees resolve to the same `budget` sources as in `ver-android`.

| App (incl. `gb` SDK) | Saved Kotlin LOC | Saved (of total) | Total LOC |
|------------------------|-----------------:|-----------------:|----------:|
| `ver-android` | 0 | 0% | 925 |
| `ver-ios` + `sdk-ios` | 606 | 67.7% | 895 |
| `ver-mac-x64` + `sdk-mac-x64` | 631 | 47.0% | 1343 |

Generated KD outputs are listed in `kd.yml` and typically match `ignore.kd.*` (see `.gitignore`). You must run code generation before a full build (see below).

## Supported platforms

- **Android** — API 24+ (`minSdk`), `compileSdk` / `targetSdk` 35; JVM target 1.8 for the app module.
- **iOS** — deployment target 15.0+ (see `ver-ios/app/project.yml`).
- **macOS (desktop)** — **Intel x64** Kotlin/Native target in `sdk-mac-x64` (`macosX64`); **Qt 6.8+** with **Qt Quick** (`ver-mac-x64/CMakeLists.txt`).

## Install build dependencies

### Common (all platforms that use KD)

- **Node.js** — used to run the KD compiler: `node $KD_DIR/dist/app.js --file=.../kd.yml`.
- **Git** — the first run of `util/gen-kd` or `util/build-mac-x64` clones [kotlin-dialect](https://github.com/OGStudio/kotlin-dialect) to **`~/dev-cache/kd`** and checks out the **3.1** branch (see `util/step/cloneKD`).

### Android

- **JDK** — compatible with **Gradle 8.12** and **Android Gradle Plugin 8.10** (e.g. **JDK 17**).
- **Android SDK** — install via Android Studio and set `ANDROID_HOME` (or use Studio’s embedded SDK). A `ver-android/local.properties` file with `sdk.dir=...` is the usual way to point Gradle at the SDK.

### iOS

- **Xcode** (with iOS platform components).
- **XcodeGen** — used by `util/gen-ios` to generate `ver-ios/app/GitBudget.xcodeproj` from `project.yml`. Install e.g. `brew install xcodegen`.
- **Java / JDK** — for Gradle when building `sdk-ios` and the `gb` XCFramework.

Set your own **development team** in `ver-ios/app/project.yml` (`DEVELOPMENT_TEAM`) for signing; the file may contain a team ID placeholder.

### macOS desktop

- **CMake** 3.16+.
- **Qt 6.8+** with **Qt Quick** — `CMAKE_PREFIX_PATH` or a standard Qt install so `find_package(Qt6 Quick)` works.
- **Java / JDK** — for the Gradle build in `sdk-mac-x64`.
- A C++17-capable **Apple Clang** toolchain (as provided by Xcode Command Line Tools).

## Generate KD sources (`ignore.kd.*`)

Any platform that needs generated KD code should run, from the repo:

```bash
./util/gen-kd
```

This clones/updates `~/dev-cache/kd` (if needed) and runs the KD generator against `./kd.yml`.

`util/gen-ios` also runs `util/gen-kd` internally before building the iOS framework.

## Run on each platform

### Android

1. Install dependencies and generate code:

   ```bash
   ./util/gen-kd
   cd ver-android
   ./gradlew :app:assembleDebug
   ```

2. Install on a device or emulator (with `adb`):

   ```bash
   ./gradlew :app:installDebug
   ```

   Or open **`ver-android`** in **Android Studio** and use Run after `./util/gen-kd`.

### iOS

1. Full iOS prep (KD + `gb` XCFramework + Swift package zip + Xcode project):

   ```bash
   ./util/gen-ios
   cp sdk-ios/gb/build/XCFrameworks/release/gb.xcframework.zip ver-ios/app/local/gb/gb.xcframework.zip
   ```

2. Open the generated project:

   ```bash
   open ver-ios/app/GitBudget.xcodeproj
   ```

3. In Xcode, pick a simulator or device, set signing if needed, and **Run** the `gitbudget` target.

### macOS (Qt)

1. Full build (clone KD, generate, build `sdk-mac-x64`, configure and build `ver-mac-x64`):

   ```bash
   ./util/build-mac-x64
   ```

2. Launch the app:

   ```bash
   ./util/launch-mac-x64
   ```

   This runs `ver-mac-x64/build/gbM64` (created by CMake; output directory is `ver-mac-x64/build/`).

For **incremental** desktop rebuilds after the first `build-mac-x64` (skip KD/SDK reconfigure): **`./util/make-mac-x64`** re-runs `make` in the existing `ver-mac-x64/build` directory.

---

`util/do-tmux` opens a tmux session with several windows for editing `ver-android`, `sdk-ios` / `ver-ios`, and `sdk-mac-x64` / `ver-mac-x64` in parallel; optional for day-to-day development.
