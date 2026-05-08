Git Budget is a Kotlin Multiplatform application.
Uses **Kotlin Dialect** (KD) — a custom state-machine/reducer framework.

# Approaches to file hierarchies

There are two approaches:
* Component approach: place original source code into `components/<componentName>` directory
* Legacy approach: place original source code in ver-android, ver-ios, ver-mac-x64, ver-browser

## Component approach

`components/` directory currently only contains `git/` directory which contains the following parts:

* `android/` directory: Kotlin code with Android platform specific dependencies like HTTP loading, WebSocket communication, file I/O, etc.
* `sdk/` directory: Cross-platform Kotlin code that is used for android, browser, desktop, ios, cannot contain any Java related stuff or platform specific stuff

### Git component structure (as an example for all components)

* git/sdk/gitConst.kt: constants used in git* files
* git/sdk/gitProto.kt: contains controller instance and registers shoulds (reducers) into the controller
* git/sdk/gitShould.kt: contains shoulds (reducers) to transition component's state from A to B
* git/android/git.kt: contains bindings of events to effect functions
* git/android/gitEffects.kt: contains effect functions that have platform specific code (in this case Android specific)

## Legacy approach

Most of the time source code lives in **`ver-android/app/src/main/kotlin/ru/iva/`**.
`sdk-ios/`, `sdk-mac-x64/`, and `sdk-windows-x64/` contain **symlinks** pointing back to `ver-android/`.
**Edit files in `ver-android/` for legacy** — editing symlink targets will appear to work but won't show in `git status` for the canonical path.

Files staring with `ignore` are not tracked in git. `ignore` files should not be edited because they come from `kd.yml` after generation.

## Code generation

`kd.yml` defines data structs and contexts. Regenerate after changes:

```
./util/gen-kd
```
So, GitContext, MasterContext, BudgetContext come from kd.yml. No need
to write them by hand.

## Architecture: Kotlin Dialect

- **Contexts** (`*Context` data classes in `ignore.kd.kt`) hold all app state
- **should-functions** (`*Should*` in `gitShould.kt`, etc.) are reducers: check `c.recentField` against `F.*` constants, return mutated context
- **`F` object** (in `ignore.kd.kt`) contains string constants for all field names
- `KDController` manages the event queue: `set(fieldName, value)` → processes all registered functions → fires callbacks

## Conventions

- Package: `org.opengamestudio`
- File naming: `*Fun.kt` (SDK functions), `*Should.kt` (SDK reducers), `*Const.kt` (SDK constants), `*Proto.kt` (SDK component prototype)
- `*Should.kt` functions: each handles one cross-cutting concern; uses `c.recentField == F.someField` pattern; ends with `c.recentField = F.none` for no-op
