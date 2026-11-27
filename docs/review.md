# Code review guide

This guide records 147 rules and decisions from the sibling `mj` project and
their application in Git Budget.
Review changed code against the applicable rules and cite the rule ID in each
finding. Use the scope and exception notes to decide whether a rule applies to
the app, platform, or file under review.

## How to apply the rules

| Scope | Application |
| --- | --- |
| Active | Apply across the rule's stated scope. |
| Scoped | Apply to the named app, platform, or file role. |
| Mixed | Check the documented exception or competing precedent. |
| Pre-import | Use the original decision in its documented context. |
| Historical | Use as migration context and follow the current replacement. |

The check labels describe how to verify a rule.

| Check | Verification |
| --- | --- |
| Auto | Check syntax or declaration order and account for documented exceptions. |
| Config | Check an explicit mapping, ownership rule, or allowlist. |
| Heuristic | Use judgment and verify the local context. |
| Runtime | Exercise the behavior on the target platform. |
| CI | Verify through the build or test workflow. |
| Review | Compare behavior, structure, or intent directly. |

Start with syntax and source ownership. Confirm which components, generated
outputs, and platform exceptions are in scope before expanding a check.
For platform changes, record source comparisons, build results, and runtime
observations separately. See R130–R134 for the platform review workflow.

Examples preserve context from the audit. Git diffs and snapshots refer to
revisions in `mj` unless identified as Git Budget evidence. Representative,
illustrative, reconstructed, and workflow examples demonstrate the intended
change or verification.

## Rule index

- [Formatting and declaration order](#formatting-and-declaration-order) (R001–R027)
- [Naming and identity](#naming-and-identity) (R028–R038)
- [Ownership, source reuse and boundaries](#ownership-source-reuse-and-boundaries) (R039–R053)
- [Dialect schema, generation and effects](#dialect-schema-generation-and-effects) (R054–R073)
- [Mirroring and native UI](#mirroring-and-native-ui) (R074–R087)
- [Rendering, native interop and threading](#rendering-native-interop-and-threading) (R088–R107)
- [App-specific gameplay and user experience](#app-specific-gameplay-and-user-experience) (R108–R116)
- [Build, dependency and packaging conventions](#build-dependency-and-packaging-conventions) (R117–R129)
- [Review requirements and pre-import decisions](#review-requirements-and-pre-import-decisions) (R130–R147)
- [Git Budget Windows verification record](#git-budget-windows-verification-record)

## Formatting and declaration order

### R001 Imports across languages

**Active · Auto**

Sort by actual module or imported path. Ignore attributes such as `@_spi(WinRTInternal)` when computing the key. Sort within each platform conditional branch.

**Review check and exceptions**

Compare adjacent declarations using parsed names. Keep conditional builds and any required import order intact.

**Example** (Git diff `33292ee` · `components/render/windows/view/RenderGLView.swift`)

**Before**

```text
import RaylibBridge
@_spi(WinRTInternal) import WinUI
import WindowsFoundation

final class RenderGLView: FrameworkElement {
    var rootChanged: EventCleanup?
    var st = GLBriState()
```

**After**

```text
import RaylibBridge
import WindowsFoundation
@_spi(WinRTInternal) import WinUI

final class RenderGLView: FrameworkElement {
    var rootChanged: EventCleanup?
    var st = GLBriState()
```

**Evidence** `6d7965a` `33292ee` `deb39c7` `47ac384`

### R002 Native includes

**Active · Auto**

Sort include names alphabetically inside an independent include group. Preserve compiler-required ordering, including macros that must precede their headers and SDK include prerequisites.

**Review check and exceptions**

Require annotated dependency-order exceptions or a bridge-specific allowlist. Never blindly reorder module maps or preprocessor blocks.

**Example** (Git diff `6d7965a` · `hello/hello-raylib/ver-android/app/src/main/c/raybri.c`)

**Before**

```text
#include <jni.h>
#include <android/log.h>

#define TRACELOG(level, ...) __android_log_print(ANDROID_LOG_INFO, "raybri", __VA_ARGS__)
```

**After**

```text
#include <android/log.h>
#include <jni.h>

#define TRACELOG(level, ...) __android_log_print(ANDROID_LOG_INFO, "raybri", __VA_ARGS__)
```

**Evidence** `6d7965a` `ba2867e` `37b9871` `c6e69f8`

### R003 Kotlin import compaction

**Active · Auto**

Combine multiple imports from the same package into a wildcard when unambiguous, including generated symbols. Subpackages have separate imports.

**Review check and exceptions**

Resolve collisions before replacing explicit names. The user did not specify an arbitrary minimum threshold beyond multiple imports.

**Example** (Reconstructed from chat · Kotlin imports)

**Before**

```text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
```

**After**

```text
import androidx.compose.foundation.layout.*
```

**Evidence** Chat · hello-platform Android import wildcard request · codestyle.md

### R004 Class and object methods

**Active · Auto**

Constructors and initializer blocks precede ordinary methods. Sort methods alphabetically within each class or object, including lifecycle callbacks.

**Review check and exceptions**

Exempt declaration order required by the compiler. Properties remain outside this sort.

**Example** (Git snapshots · selected matching lines `b216ac7` · `components/render/android/view/RenderGLView.kt`)

**Before**

```text
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
…
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
…
    override fun onDrawFrame(gl: GL10?) {
…
    override fun onPause() {
```

**After**

```text
    override fun onDrawFrame(gl: GL10?) {
…
    override fun onPause() {
…
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
…
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
```

**Evidence** `b216ac7` `16b3cfe` · components-over-platforms.md

### R005 Auxiliary functions

**Active · Auto**

Sort free functions in each component's Aux file alphabetically by function name.

**Review check and exceptions**

Preserve conditional groups, overloads and attached comments.

**Example** (Git diff `16b3cfe` · `components/render/linux/renderAux.swift`)

**Before**

```text
func renderGLAreaPtr(_ view: RenderGLView) -> UnsafeMutablePointer<GtkGLArea> {
    UnsafeMutableRawPointer(view.widgetPointer).assumingMemoryBound(to: GtkGLArea.self)
}

func renderGLView(ptr: UnsafeMutableRawPointer?) -> RenderGLView {
    Unmanaged<RenderGLView>.fromOpaque(ptr!).takeUnretainedValue()
}

func renderDraw(_ vm: RenderVM) {
    // GTK binds its framebuffer before invoking the render callback
```

**After**

```text
func renderDraw(_ vm: RenderVM) {
    // GTK binds its framebuffer before invoking the render callback
    raybriRender(Int32(vm.pixelSize[0]), Int32(vm.pixelSize[1]))
}

func renderGLAreaPtr(_ view: RenderGLView) -> UnsafeMutablePointer<GtkGLArea> {
    UnsafeMutableRawPointer(view.widgetPointer).assumingMemoryBound(to: GtkGLArea.self)
}

func renderGLView(ptr: UnsafeMutableRawPointer?) -> RenderGLView {
    Unmanaged<RenderGLView>.fromOpaque(ptr!).takeUnretainedValue()
}
```

**Evidence** `16b3cfe` `30ab07e`

### R006 Calculation functions

**Active · Auto**

Sort free functions in each component's Fun file alphabetically by function name.

**Review check and exceptions**

Separate from Aux sorting and class member sorting.

**Example** (Git snapshots · selected matching lines `30ab07e` · `components/hcm/hcmFun.swift`)

**Before**

```text
func hcmGenerateItems() -> [HCMItem] {
…
func hcmGenerateItemColors(_ items: [HCMItem]) -> [HCMItemColor] {
…
func hcmResetItemPositions(_ uiSize: [Float]) -> [HCMItemPosition] {
…
func hcmItemId(
```

**After**

```text
func hcmGenerateItemColors(_ items: [HCMItem]) -> [HCMItemColor] {
…
func hcmGenerateItems() -> [HCMItem] {
…
func hcmItemId(
…
func hcmResetItemPositions(_ uiSize: [Float]) -> [HCMItemPosition] {
```

**Evidence** `30ab07e`

### R007 Constants

**Active · Auto**

Sort constant declarations alphabetically by identifier, including HCM constants.

**Review check and exceptions**

Where declarations have dependencies, flag ordering for review before autofix. Preserve macro/include dependencies.

**Example** (Git snapshots · selected matching lines `19fb68d` · `components/hcm/hcmConst.swift`)

**Before**

```text
let HCM_ITEMS_COUNT = 16
…
let HCM_ITEM_COLOR_PALETTE = [
```

**After**

```text
let HCM_ITEM_COLOR_PALETTE = [
…
let HCM_ITEMS_COUNT = 16
```

**Evidence** `19fb68d`

### R008 RenderVM fields

**Scoped · Config**

Alphabetize ordinary VM fields in the audited HCM RenderVM, including new gameplay fields. Keep native frame fields together while preserving platform branches, and keep singleton/bootstrap machinery separate.

**Review check and exceptions**

Start with an app-specific rule for property groups. Do not impose a cross-repository alphabetical property rule without more evidence.

**Example** (Illustrative · HCM Swift RenderVM field order)

**Before**

```text
var matchedItemIds = [Int]()
var hiddenItemIds = [Int]()
var itemPositions = [HCMItemPosition]()
```

**After**

```text
var hiddenItemIds = [Int]()
var itemPositions = [HCMItemPosition]()
var matchedItemIds = [Int]()
```

**Evidence** Chat · “and renderVM, too” · `e275c18` `59e9239` `1c7d8df`

### R009 Android Gradle blocks

**Scoped · Auto**

Sort peer configuration properties, such as compileSdk and namespace, alphabetically within the relevant block.

**Review check and exceptions**

Avoid reordering imperative statements, dependent assignments and build phases.

**Example** (Git snapshots · selected matching lines `78cb1dc` · `hello/hello-raylib/ver-android/app/build.gradle.kts`)

**Before**

```text
    namespace = "org.opengamestudio"
…
    compileSdk = 35
…
    ndkVersion = "28.2.13676358"
```

**After**

```text
    compileSdk = 35
…
    namespace = "org.opengamestudio"
…
    ndkVersion = "28.2.13676358"
```

**Evidence** `78cb1dc`

### R010 SwiftUI modifiers

**Active · Auto**

Put every modifier on a separate line indented one level from its view, including a solitary .ignoresSafeArea(). Keep semantic modifier order.

**Review check and exceptions**

Recognize view chains rather than flagging every chained Swift call. Apply the same layout to mirrored SwiftCrossUI UI.

**Example** (Git diff `c8496db` · `hello/hello-raylib/ver-mac/src/HRBApp.swift`)

**Before**

```text
ZStack {
    RenderView().ignoresSafeArea()
    HRBView(appDelegate.hrbVM)
```

**After**

```text
ZStack {
    RenderView()
        .ignoresSafeArea()
    HRBView(appDelegate.hrbVM)
```

**Evidence** `2bb1aa0` `c8496db` `9879118`

### R011 Compose named arguments

**Active · Auto**

Sort named composable arguments alphabetically, including modifier between horizontalAlignment and verticalArrangement.

**Review check and exceptions**

Preserve positional arguments and modifier chain order. Reordering arguments with side effects requires review.

**Example** (Git diff `298884a` · `components/hrb/android/view/HRBView.kt`)

**Before**

```text
Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.padding(40.dp)
) {
    Box(contentAlignment = Alignment.Center) {
```

**After**

```text
Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(40.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    Box(contentAlignment = Alignment.Center) {
```

**Evidence** `298884a` · ui.md

### R012 Multiline C parameters

**Active · Auto**

In a C declaration or definition with two or more parameters, put each parameter on its own line. Include JNI functions.

**Review check and exceptions**

The request concerned parameter declarations, not every call site.

**Example** (Git diff `f06dc3b` · `components/raybri/raybriSetup2DFramebuffer.h`)

**Before**

```text
// Header

void raybriSetup2DFramebuffer(int width, int height);

// Source

#if defined(RAYBRI_SETUP_2D_FRAMEBUFFER_IMPLEMENTATION)

void raybriSetup2DFramebuffer(int width, int height) {
    // Match the viewport and projection to the current backing size
    rlViewport(0, 0, width, height);
```

**After**

```text
// Header

void raybriSetup2DFramebuffer(
    int width,
    int height
);

// Source

#if defined(RAYBRI_SETUP_2D_FRAMEBUFFER_IMPLEMENTATION)

void raybriSetup2DFramebuffer(
    int width,
    int height
) {
    // Match the viewport and projection to the current backing size
    rlViewport(0, 0, width, height);
```

**Evidence** `f06dc3b`

### R013 Multiline Swift calculation parameters

**Active · Auto**

In a component Fun Swift declaration with two or more parameters, put each on its own line.

**Review check and exceptions**

It does not impose the same rule on all Swift call sites or unrelated helpers.

**Example** (Git diff `3444159` · `components/render/mac/renderFun.swift`)

**Before**

```text
// Converts a position from framebuffer coordinates into scene coordinates for interpreting pointer input consistently within the currently displayed scene view
func renderCoordFBtoSC(_ position: [Float], _ fbSize: [Float], _ cameraPosition: [Float], _ scale: [Float]) -> [Float] {
    return [
        (position[0] - fbSize[0] / 2) / scale[0] + cameraPosition[0],
        (position[1] - fbSize[1] / 2) / scale[1] + cameraPosition[1]
    ]
}

// Converts a position from scene coordinates into framebuffer coordinates for placing scene content correctly within the currently displayed rendering area
```

**After**

```text
// Converts a position from framebuffer coordinates into scene coordinates for interpreting pointer input consistently within the currently displayed scene view
func renderCoordFBtoSC(
    _ position: [Float],
    _ fbSize: [Float],
    _ cameraPosition: [Float],
    _ scale: [Float]
) -> [Float] {
    return [
        (position[0] - fbSize[0] / 2) / scale[0] + cameraPosition[0],
        (position[1] - fbSize[1] / 2) / scale[1] + cameraPosition[1]
    ]
}

// Converts a position from scene coordinates into framebuffer coordinates for placing scene content correctly within the currently displayed rendering area
```

**Evidence** `3444159` · codestyle.md

### R014 Compound Swift if

**Active · Auto**

With two or more logical conditions put if on a line by itself, each condition on its own indented line, the opening brace on its own line, and `&&` or `||` at the end of the preceding condition.

**Review check and exceptions**

Parse Bool operators and multiline conditions. A simple if can stay on one line.

**Example** (Git diff `ca3f8d0` · `components/hcm/hcmFun.swift`)

**Before**

```text
    _ position: [Float]
) -> Int {
    let x = position[0]
    let y = position[1]
    for index in itemPositions.indices {
        let item = itemPositions[index]
        if x >= item.x && x < item.x + item.width &&
            y >= item.y && y < item.y + item.height {
            return index
        }
    }
    return -1
}
```

**After**

```text
    _ position: [Float]
) -> Int {
    let x = position[0]
    let y = position[1]
    for index in itemPositions.indices {
        let item = itemPositions[index]
        if
            x >= item.x &&
            x < item.x + item.width &&
            y >= item.y &&
            y < item.y + item.height
        {
            return index
        }
    }
    return -1
}
```

**Evidence** `ca3f8d0`

### R015 Blank lines

**Active · Auto**

Allow at most one consecutive blank line between declarations or sections, across platforms and file types.

**Review check and exceptions**

Avoid changing literal text, generated artifacts and external vendor files without checking semantics.

**Example** (Git diff `ac8ec5c` · `components/render/ios/renderAux.swift`)

**Before**

```text
    return [w, h]
}


func renderRecreateSurface(_ vm: RenderVM) {
    glbriResetSurface(&vm.glView.st)
    glbriMakeCurrentContext(&vm.glView.st)
```

**After**

```text
    return [w, h]
}

func renderRecreateSurface(_ vm: RenderVM) {
    glbriResetSurface(&vm.glView.st)
    glbriMakeCurrentContext(&vm.glView.st)
```

**Evidence** `ac8ec5c`

### R016 Comment punctuation

**Active · Auto**

Code comments do not end with a dot.

**Review check and exceptions**

Check the end of a prose comment, allowing URLs, decimals and code tokens where punctuation is data.

**Example** (Git diff `dbfccc2` · `components/raybri/mac/raybri.c`)

**Before**

```text
// rlgl is provided by the linked Homebrew raylib library.
```

**After**

```text
// rlgl is provided by the linked Homebrew raylib library
```

**Evidence** `dbfccc2`

### R017 Comment voice

**Active · Heuristic**

Start action comments with the imperative form such as Convert, Return, Apply or Provide.

**Review check and exceptions**

Apply to action comments, not every standalone note. Advisory check for forms such as Converts or Returns.

**Example** (Git diff `b6fbdf5` · `components/render/mac/renderFun.swift`)

**Before**

```text
// Converts a position from framebuffer coordinates into scene coordinates for interpreting pointer input consistently within the currently displayed scene view
```

**After**

```text
// Convert a position from framebuffer coordinates into scene coordinates for interpreting pointer input consistently within the currently displayed scene view
```

**Evidence** `b6fbdf5`

### R018 Coordinate helper comments

**Scoped · Review**

Give renderCoord functions a short purpose comment. Describe what space is converted and what the helper does in generic terms. Keep formula details out of these comments.

**Review check and exceptions**

The conversation requested about 20 words. The enforceable pattern is coverage and concise purpose, not exactly 20 tokens.

**Example** (Git diff `a9d0a57` · `components/render/mac/renderFun.swift`)

**Before**

```text
// Converts framebuffer pixels into scene coordinates by removing viewport centering, dividing by display and zoom scale, then adding camera position
func renderCoordFBtoSC(_ position: [Float], _ fbSize: [Float], _ cameraPosition: [Float], _ scale: [Float]) -> [Float] {
    return [
        (position[0] - fbSize[0] / 2) / scale[0] + cameraPosition[0],
        (position[1] - fbSize[1] / 2) / scale[1] + cameraPosition[1]
    ]
}

// Converts scene coordinates into framebuffer pixels by subtracting camera position, multiplying by display and zoom scale, then adding viewport centering
func renderCoordSCtoFB(_ position: [Float], _ fbSize: [Float], _ cameraPosition: [Float], _ scale: [Float]) -> [Float] {
```

**After**

```text
// Converts a position from framebuffer coordinates into scene coordinates for interpreting pointer input consistently within the currently displayed scene view
func renderCoordFBtoSC(_ position: [Float], _ fbSize: [Float], _ cameraPosition: [Float], _ scale: [Float]) -> [Float] {
    return [
        (position[0] - fbSize[0] / 2) / scale[0] + cameraPosition[0],
        (position[1] - fbSize[1] / 2) / scale[1] + cameraPosition[1]
    ]
}

// Converts a position from scene coordinates into framebuffer coordinates for placing scene content correctly within the currently displayed rendering area
func renderCoordSCtoFB(_ position: [Float], _ fbSize: [Float], _ cameraPosition: [Float], _ scale: [Float]) -> [Float] {
```

**Evidence** `05f39c8` `a9d0a57`

### R019 GTK callback closure layout

**Scoped · Auto**

Keep the C callback's parameter list, including unused underscores and this, on the same declaration line as let and the closure opening.

**Review check and exceptions**

Match the render signal registration format. This is not a general rule for multiline Swift closures.

**Example** (Git diff `41b59ed` · `components/render/linux/renderAux.swift`)

**Before**

```text
func renderSetupGtkSignals(_ view: RenderGLView) {
    let pressed: @convention(c) (RawPtr?, Int32, Double, Double, RawPtr?) -> Void = {
        _, _, x, y, this in
        renderGLView(ptr: this).onMouseDown(x, y)
    }
```

**After**

```text
func renderSetupGtkSignals(_ view: RenderGLView) {
    let pressed: @convention(c) (RawPtr?, Int32, Double, Double, RawPtr?) -> Void = { _, _, x, y, this in
        renderGLView(ptr: this).onMouseDown(x, y)
    }
```

**Evidence** `41b59ed`

### R020 GTK callback groups

**Scoped · Config**

Group lifecycle callbacks and signal registration separately from click wiring. Give each group a short comment of at most 10 words.

**Review check and exceptions**

Apply to renderSetupGtkSignals. Avoid duplicating a callback just to achieve ordering.

**Example** (Git snapshots · selected matching lines `2d06ea6` · `components/render/linux/renderAux.swift`)

**Before**

```text
    let realize: @convention(c) (RawPtr?, RawPtr?) -> Void = { _, this in
…
    let pressed: @convention(c) (RawPtr?, Int32, Double, Double, RawPtr?) -> Void = { _, _, x, y, this in
```

**After**

```text
    // Connect GL area lifecycle signals
…
    let realize: @convention(c) (RawPtr?, RawPtr?) -> Void = { _, this in
…
    // Connect the mouse press signal
…
    let pressed: @convention(c) (RawPtr?, Int32, Double, Double, RawPtr?) -> Void = { _, _, x, y, this in
```

**Evidence** `4ebcc56` `2d06ea6`

### R021 Kotlin function bodies

**Active · Auto**

Use normal braced function bodies and an explicit return for results. Reject fun … = … expression bodies.

**Review check and exceptions**

Does not ban property initializers, lambdas or expression arguments.

**Example** (Git diff `dd9e96c` · `components/other/other.kt`)

**Before**

```text
}

fun otherUUIDString(): String = UUID.randomUUID().toString()
```

**After**

```text
}

fun otherUUIDString(): String {
    return UUID.randomUUID().toString()
}
```

**Evidence** `f7b66d2` `dd9e96c`

### R022 Swift function bodies

**Mixed · Config**

Ordinary value-returning helpers use braced bodies and explicit return. Keep Swift declarative view-builder expressions in their natural syntax. The user explicitly removed return from HCM's statsLabel.

**Review check and exceptions**

Skip generated code and view-builder bodies. Allowlist statsLabel before any repository-wide enforcement.

**Example** (Git diff `fe81f89` · `components/hcm/mac/view/HCMView.swift`)

**Before**

```text
func statsLabel(_ value: String) -> some View {
    return ZStack(alignment: .topLeading) {
        Text(value)
```

**After**

```text
func statsLabel(_ value: String) -> some View {
    ZStack(alignment: .topLeading) {
        Text(value)
```

**Evidence** `dd9e96c` `fe81f89` · codestyle.md

### R023 Swift access level

**Active · Heuristic**

Use Swift's default access for internal helpers. Do not add private merely because a helper is file-local.

**Review check and exceptions**

Specific private properties, GTK RawPtr alias, narrow setters and public adapters can have explicit access when required.

**Example** (Git diff `eca3f30` · `components/hcm/hcmFun.swift`)

**Before**

```text
private func hcmColumnCount(
    width: Float,
    height: Float
```

**After**

```text
func hcmColumnCount(
    width: Float,
    height: Float
```

**Evidence** `eca3f30` `a8d0f95` · AGENTS.md

### R024 Straight loops

**Scoped · Heuristic**

Use ordinary for loops in HCM calculation functions instead of converting their core computations into collection pipelines.

**Review check and exceptions**

Standard library operations such as shuffled() are allowed. Apply to HCM Fun by name, not every collection call in Swift/Kotlin.

**Example** (Reconstructed from chat · HCM calculation)

**Before**

```text
return items.map { HCM_ITEM_COLOR_PALETTE[$0.groupId] }
```

**After**

```text
var itemColors: [HCMItemColor] = []
for item in items {
    itemColors.append(HCM_ITEM_COLOR_PALETTE[item.groupId])
}
return itemColors
```

**Evidence** Chat · “use for loop in all hcmFun functions” · `7af16c4` `e199cb4`

### R025 Avoid unnecessary generics

**Active · Heuristic**

Use concrete types and normal functions when no caller needs generality.

**Review check and exceptions**

Allow generics for actual generic operations and UI wrappers such as a view-accepting number roll.

**Example** (Git diff `f7b66d2` · `components/hcm/android/hcmFun.kt`)

**Before**

```text
}

fun <T> hcmCount(items: Array<T>): Int = items.size

fun hcmDeselectFirstItem(selectionPair: Array<Int>): Array<Int> =
```

**After**

```text
}

fun hcmCount(items: Array<HCMItem>): Int {
    return items.size
}

fun hcmCount(items: Array<Int>): Int {
    return items.size
}
```

**Evidence** Chat · “don't use generics where it's not necessary” · `f7b66d2` `516ae1e`

### R026 One-use values

**Active · Heuristic**

Inline a trivial property alias or pointer assignment used once when it adds no meaning. Cache repeated native pointer queries in a let, as in GTK init and onRealize.

**Review check and exceptions**

One-use named results can be justified for thread/API boundaries or deliberate readability. No blanket unused-reuse rule.

**Example** (Git diff `645565a` · `hello/hello-color-memory/ver-linux/src/HCMApp.swift`)

**Before**

```text
var body: some Scene {
    let isRestartVisible = vm.isRestartVisible
    WindowGroup("hello-color-memory") {
```

**After**

```text
var body: some Scene {
    WindowGroup("hello-color-memory") {
```

**Evidence** `a099048` `9468beb` `d476788` `75499de` `645565a` `9020107`

### R027 Stray Swift punctuation

**Scoped · Auto**

Remove the trailing semicolon from the mirrored didMoveToWindow line. Keep a short fatalError guard on one line where the user requested that layout.

**Review check and exceptions**

These are targeted formatting decisions, insufficient evidence for a blanket all-guards-on-one-line style.

**Example** (Reconstructed from chat · Swift statements)

**Before**

```text
super.didMoveToWindow();
guard let context else {
    fatalError("Missing context")
}
```

**After**

```text
super.didMoveToWindow()
guard let context else { fatalError("Missing context") }
```

**Evidence** Chat · didMoveToWindow and guard else fatalError

## Naming and identity

### R028 App abbreviation

**Active · Config**

Carry the selected app acronym consistently through component, app, AppDelegate, view, VM and YAML context. Established names are HP, HRB, HST and HCM. Component directories and free-function prefixes are lowercase.

**Review check and exceptions**

Store the chosen mapping. Do not derive an acronym algorithmically from the app folder.

**Example** (Git diff `0934da1` · `hello/hello-selection/ver-mac/src/HSTApp.swift`)

**Before**

```text
@main
struct SelectionApp: App {
    @NSApplicationDelegateAdaptor(SelectionAppDelegate.self) private var appDelegate

    var body: some Scene {
```

**After**

```text
@main
struct HSTApp: App {
    @NSApplicationDelegateAdaptor(HSTAppDelegate.self) private var appDelegate

    var body: some Scene {
```

**Evidence** `423a024` `0934da1` `0b3f047` `bf52dd9`

### R029 Display and app identity

**Active · Config**

Keep the approved spelling and case across the UI, platform metadata, target, resources and scripts wherever the same name is intended. Android's application ID, namespace, Gradle display name and executable name remain distinct fields.

**Review check and exceptions**

Compare configured identity mappings. Examples include OGS Blue capitalization and hello-raylib as HRB title.

**Example** (Git diff `43b4848` · `hello/hello-raylib/dialect.yml`)

**Before**

```text
        then:
            c.title = "Blue"
didLaunch:
```

**After**

```text
        then:
            c.title = "hello-raylib"
didLaunch:
```

**Evidence** `43b4848` `0b3f047` `6a49531` · Chat · OGS Blue capitalization

### R030 Presentation title

**Scoped · Auto**

Use title for the value HST/HRB/HCM display, including generated fields. Avoid restoring titleText or a setter named hstUpdateTitle.

**Review check and exceptions**

Do not change HP's separately meaningful countText or legitimate label fields.

**Example** (Git diff `97e8dea` · `hello/hello-raylib/dialect.yml`)

**Before**

```text
type: context
fields:
    titleText: String
    didLaunch: Bool
    didSetup: Bool
```

**After**

```text
type: context
fields:
    title: String
    didLaunch: Bool
    didSetup: Bool
```

**Evidence** `97e8dea` `9baa298`

### R031 Same name through handoff

**Active · Heuristic**

Preserve the original field name across contexts, effects, receiving VM and drawing when the meaning is unchanged.

**Review check and exceptions**

Permit intentional transformations like mouseDownPos → pointerDownPos, or pixelSize → computed uiSize. Compare data flow, not spelling alone.

**Example** (Git diff `a78a1e9` · `hello/hello-selection/dialect.yml`)

**Before**

```text
- didPrepareGL: renderStartTimer(RenderVM.shared!)
- items: RenderVM.shared!.quads = c.items
- pointerDownPos: hstSet(F.pointerDownPos, c.pointerDownPos)
```

**After**

```text
- didPrepareGL: renderStartTimer(RenderVM.shared!)
- items: RenderVM.shared!.items = c.items
- pointerDownPos: hstSet(F.pointerDownPos, c.pointerDownPos)
```

**Evidence** `b3795e0` `a78a1e9` `dd98dc3`

### R032 Item identity vs geometry

**Scoped · Config**

Use selectedItemId, items, itemPositions and HCMItemPosition as appropriate. Use itemPositions for geometry and items for gameplay data where both exist. Avoid selectedQuadId, RenderQuad aliases and a duplicate HSTQuad.

**Review check and exceptions**

HST's HSTItem and HCM's HCMItemPosition are intentionally different types.

**Example** (Git diff `96cd623` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
didLaunch: Bool
didSetup: Bool
itemPositions: [HCMItem]
pointerDownPos: [Float]
selectedItemId: Int
```

**After**

```text
didLaunch: Bool
didSetup: Bool
itemPositions: [HCMItemPosition]
pointerDownPos: [Float]
selectedItemId: Int
```

**Evidence** `1d1f92e` `51bd709` `33e2df1` `dd98dc3` `96cd623`

### R033 Coordinate names

**Scoped · Config**

Use UI, FB and SC for local UI, framebuffer and scene coordinates. Conversion helpers use renderCoord prefix and lowercase to between space tokens, for example renderCoordUItoFB.

**Review check and exceptions**

Keep existing operation suffixes including Scale. Do not infer a reverse scale helper from symmetry.

**Example** (Git diff `6e44ecd` · `hello/hello-selection/dialect.yml`)

**Before**

```text
        then:
            c.pointerDownPos = renderPixelToWorldPosition(renderConvertUIPosition(c.mouseDownPos), RenderVM.shared!)
willRecreateSurface:
```

**After**

```text
        then:
            c.pointerDownPos = renderCoordFBtoSC(renderCoordUItoFB(c.mouseDownPos, RenderVM.shared!), RenderVM.shared!)
willRecreateSurface:
```

**Evidence** `6e44ecd` `05794d1` · Chat · remove Convert and use to

### R034 Native pointer helpers

**Scoped · Config**

Use renderGLAreaPtr for GtkGLArea conversion and renderGLView(ptr:) for GTK userdata recovery. Use renderViewPtr for mirrored EGL host-view extraction on iOS and Windows. Reuse window as GLBriState's native surface field.

**Review check and exceptions**

Check platform-specific overloads. Scope RawPtr convenience alias to GTK callback types.

**Example** (Git diff `15b90b0` · `components/render/ios/renderEffect.swift`)

**Before**

```text
// On iOS EGL/ANGLE renders into the UIView's layer
let window = renderPtr(vm.glView)
vm.glView.st.window = window
```

**After**

```text
// On iOS EGL/ANGLE renders into the UIView's layer
let window = renderViewPtr(vm.glView)
vm.glView.st.window = window
```

**Evidence** `a8d0f95` `ec7a52f` `15b90b0` `290c0ea`

### R035 Bridge and operation names

**Scoped · Config**

Name the native translation unit raybri.c, the EGL bridge glbri, the clear-only bridge call raybriRenderEmpty, the projection raybriSetup2DFramebuffer and HCM's background call raybriDrawBackgroundCentralRadialGradient.

**Review check and exceptions**

Validate declarations, macros, target paths and JNI facades after renaming.

**Example** (Git diff `607c7bb` · `hello/hello-raylib/ver-mac/Package.swift`)

**Before**

```text
path: "src",
sources: ["Raylib.c"],
publicHeadersPath: "raybri",
```

**After**

```text
path: "src",
sources: ["raybri.c"],
publicHeadersPath: "raybri",
```

**Evidence** `607c7bb` `bdeec36` `53b9090` `ae7d211` `229c6e6`

### R036 GL scheduling fields

**Scoped · Config**

Use frameCallback for the native Linux/Windows token and frameTimer for the Apple timer. Keep them adjacent across platform branches. Remove unused startTime and retain matchStartTime for HCM's active animation.

**Review check and exceptions**

Verify all hello consumers when refactoring a shared effect.

**Example** (Git diff `1c7d8df` · `hello/hello-raylib/ver-linux/src/render/renderVM.swift`)

**Before**

```text
lazy var glView = RenderGLView()
var pixelSize = [Int]()
var tickCallback: UInt32?

static private(set) weak var shared: RenderVM?
```

**After**

```text
lazy var glView = RenderGLView()
var pixelSize = [Int]()

static private(set) weak var shared: RenderVM?
```

**Evidence** `59e9239` `1c7d8df`

### R037 HCM helper naming

**Scoped · Config**

Use hcmResetItemPositions for recomputation on resize, hcmGenerateItemColors for generating colors, hcmIsPairMatching(items, selectionPair) with items first, renderResetUISize for Render measurement publication.

**Review check and exceptions**

These are concrete API and verb choices, not abstract naming grammar.

**Example** (Git diff `3f1d089` · `components/hcm/hcmFun.swift`)

**Before**

```text
}

func hcmResetItemColors(_ items: [HCMItem]) -> [HCMItemColor] {
    var itemColors: [HCMItemColor] = []
    for item in items {
```

**After**

```text
}

func hcmGenerateItemColors(_ items: [HCMItem]) -> [HCMItemColor] {
    var itemColors: [HCMItemColor] = []
    for item in items {
```

**Evidence** `3201269` `dd98dc3` `3f1d089` `08c75e5`

### R038 File role names

**Active · Config**

Match the component prefix in component, VM, Const, Aux, Fun and Effect file names. Put RenderGLView under Render and named native/representable views in their established role files.

**Review check and exceptions**

Keep Kotlin capitalization close to the Swift counterpart while honoring native entry points such as MainActivity.

**Example** (Git diff `7ef30e7` · `hello/hello-color-memory/ver-android/app/src/main/kotlin/org/opengamestudio/MainActivity.kt`)

**Before**

```text
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HCMActivityDelegate.setup(this)

        enableEdgeToEdge(
```

**After**

```text
class MainActivity: ComponentActivity() {
    val impl = HCMAppImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        impl.setup(this)

        enableEdgeToEdge(
```

**Evidence** `044945a` `378c7db` `469d231` `7ef30e7` `bc20881`

## Ownership, source reuse and boundaries

### R039 RenderGLView owner

**Active · Config**

RenderGLView belongs to `components/render/<platform>/view`, including when an app needs new pointer input. Update the existing native implementation and link to it.

**Review check and exceptions**

Reject ownership under HST/HCM/HRB or another app domain.

**Example** (Git diff `044945a` · `hello/hello-selection/ver-mac/src/render/view/RenderGLView.swift`)

**Before**

```text
../../../../../../components/hst/mac/view/RenderGLView.swift
```

**After**

```text
../../../../../../components/render/mac/view/RenderGLView.swift
```

**Evidence** `044945a`

### R040 Native input reports facts

**Active · Heuristic**

Native handlers use renderSet to report positions, generic lifecycle, and measurement events. They do not choose game items, compute outcomes, dispatch concrete selection or implement domain hit testing.

**Review check and exceptions**

Permit coordinate adaptation and Render context forwarding.

**Example** (Representative excerpts · native callback and domain rule)

**Before**

```text
// RenderGLView.swift
if let id = hstQuadId(/* pointer and bounds */) {
    hstSet(F.selectedItemId, id)
}
```

**After**

```text
// RenderGLView.swift
renderSet(F.mouseDownPos, position)

// HST should in dialect.yml
c.selectedItemId = hstItemId(c.items, c.pointerDownPos)
```

**Evidence** `ae56798` `cbc134e` `044945a`

### R041 VM instance ownership

**Active · Config**

Code inside a component may dereference only that component's VM instances. Render drawing reads RenderVM. App entry points and delegates may assemble or retain multiple VMs.

**Review check and exceptions**

Resolve references and enclosing owner. Distinguish instance access from imported type references and Dialect effects.

**Example** (Illustrative · Render drawing inputs)

**Before**

```text
// renderDraw reads another component's instance
let items = HSTVM.shared!.items
```

**After**

```text
// renderDraw receives its own RenderVM
let items = vm.items
// The element type can still be HSTItem
```

**Evidence** `54b94b9` `900073c` `33e2df1`

### R042 Value types across components

**Active · Heuristic**

Pass existing value types such as HSTItem or HCMItemPosition through Dialect. Do not invent duplicate Render-specific data types solely because the producer is a different component.

**Review check and exceptions**

Linter can flag added type twins. Semantic equivalence requires review.

**Example** (Git diff `33e2df1` · `hello/hello-selection/dialect.yml`)

**Before**

```text
type: component
oneliners:
    - items: renderSet(F.quads, c.items.map { RenderQuad(x: $0.x, y: $0.y, width: $0.width, height: $0.height) })
    - title: HSTVM.shared!.title = c.title
shoulds:
```

**After**

```text
type: component
oneliners:
    - items: renderSet(F.quads, c.items)
    - title: HSTVM.shared!.title = c.title
shoulds:
```

**Evidence** `33e2df1` `a78a1e9`

### R043 App-owned files

**Active · Config**

Store renderDraw, RenderVM and app-specific raybri.c under the app. Use a shared native owner only for generic code. Do not choose app behavior with flags such as `HELLO_SELECTION` in a shared file.

**Review check and exceptions**

Compatible Swift targets may link directly to their own app's macOS source.

**Example** (Representative layout · move from shared component to app)

**Before**

```text
components/render/mac/renderDrawSelection.swift
components/render/mac/renderVM.swift   # HELLO_SELECTION branch
```

**After**

```text
hello/hello-selection/ver-mac/src/render/renderDraw.swift
hello/hello-selection/ver-mac/src/render/renderVM.swift
hello/hello-selection/ver-mac/src/raybri/raybri.c
```

**Evidence** `7d801fc` `43e7ce5` `cf1254d` `59e9239`

### R044 Platform variants

**Active · Config**

If implementations differ across platforms or languages, place them in their platform subdirectories. Root is for common code with no separate platform variants. Let compatible peers link to a canonical platform owner.

**Review check and exceptions**

Explicit shared adapters with internal `#if` may be common owners, e.g. cuiDesktopSize and CUIObservedObject.

**Example** (Representative file layout · Render constants)

**Before**

```text
components/render/renderConst.swift
components/render/android/renderConst.kt
```

**After**

```text
components/render/mac/renderConst.swift
components/render/ios/renderConst.swift → ../mac/renderConst.swift
components/render/android/renderConst.kt
```

**Evidence** `1522183` `6b4668c` `f8f753f`

### R045 Relative source links

**Active · Auto**

Use tracked Git symlinks for identical reusable source, resources, wrappers and steps. Use relative paths and verify the destination resolves.

**Review check and exceptions**

Read Git mode 120000 plus target blob. Use path depth from the destination directory.

**Example** (Git snapshots · selected matching lines `9ca57de` · `hello/hello-raylib/ver-android/gradle/wrapper/gradle-wrapper.properties`)

**Before**

```text
distributionUrl=https\://services.gradle.org/distributions/gradle-9.3.1-bin.zip
```

**After**

```text
../../../../hello-platform/ver-android/gradle/wrapper/gradle-wrapper.properties
```

**Evidence** `c8d1ecf` `cc99597` `9ca57de` `1bdf0de`

### R046 Link source selection

**Active · Config**

Link reused component source directly to its canonical owner where intended. If a Windows source is the Linux-specific implementation, link to Linux. If Linux itself links to macOS, use macOS directly where instructed.

**Review check and exceptions**

Preserve intentional target→component-platform indirection. Avoid enforcing a global ban on link chains.

**Example** (Reconstructed from chat · symlink graph)

**Before**

```text
Windows → Linux → macOS    # same file on all three
```

**After**

```text
Windows → macOS           # source is owned by macOS
Windows → Linux           # separate Linux-owned view
```

**Evidence** `7d85db7` `1bdf0de` · Chat · Windows symlink origin correction

### R047 App siblings

**Active · Config**

An app’s implementation may reuse component owners and files within its own app, including its own macOS canonical sources. Do not link a source inside an app to another hello app.

**Review check and exceptions**

Explicitly distinguish shared utility step and wrapper/artwork reuse, which historically links to hello-platform.

**Example** (Representative source links · historical exception remains scoped)

**Before**

```text
hello-selection/.../ui/theme/Color.kt
  → hello-raylib/.../ui/theme/Color.kt
```

**After**

```text
hello-selection/.../render/view/RenderView.kt
  → components/render/android/view/RenderView.kt
```

**Evidence** `af9e16e` `1fd48b5` `9bfe90b` · Chat · hello targets must link to components

### R048 HCM RenderAux layering

**Scoped · Config**

Keep shared macOS and iOS Render Aux implementations separate in components. Link each via renderAuxSuper.swift in HCM. Add the app-owned renderAux.swift for renderMatchTime and share that app source where compatible.

**Review check and exceptions**

Allow the explicit Super link name. Do not move the shared owner or merge Apple variants to add one app function.

**Example** (Representative layout · Apple RenderAux owners)

**Before**

```text
components/render/{mac,ios}/renderAux.swift
  also contains the HCM-only match clock
```

**After**

```text
ver-mac/src/render/renderAuxSuper.swift → components/render/mac/renderAux.swift
ver-ios/src/render/renderAuxSuper.swift → components/render/ios/renderAux.swift
ver-mac/src/render/renderAux.swift      # renderMatchTime()
ver-ios/src/render/renderAux.swift     → ver-mac/src/render/renderAux.swift
```

**Evidence** `f215b12` `9bfe90b` `4332fc1` `b64c783`

### R049 Effect placement

**Active · Config**

Put component effects that schedule or perform state/I/O work into the corresponding Effect file, e.g. hcmAnimateDeselection and renderResetUISize. Keep the effect in shared platform Render when other consumers need the same behavior.

**Review check and exceptions**

Update the affected consumers' YAML and generated fields when changing a shared dependency.

**Example** (Representative file move · shared effect)

**Before**

```text
components/hcm/hcmAux.swift
    func hcmAnimateDeselection(…) { … }
```

**After**

```text
components/hcm/hcmEffect.swift
    func hcmAnimateDeselection(…) { … }
```

**Evidence** `3201269` `b81ebbc` `378c7db` `469d231`

### R050 Fun vs Aux purity

**Mixed · Heuristic**

General Fun helpers are pure free calculations on built-ins and arrays, without VM or platform dependencies. Aux is for native/system integration and import-requiring helpers. HCM explicitly puts import-free pure calculations using generated HCM value types in Fun.

**Review check and exceptions**

Preserve the HST historical placement rather than forcing an automatic relocation. The narrower HCM rule overrides a blanket user-type ban for that component.

**Example** (Representative file roles · HCM exception)

**Before**

```text
hcmAux.swift
    func hcmResetItemPositions(_ uiSize: [Float]) -> [HCMItemPosition]
```

**After**

```text
hcmFun.swift
    func hcmResetItemPositions(_ uiSize: [Float]) -> [HCMItemPosition]

hcmEffect.swift
    func hcmAnimateDeselection(_ uuid: String)
```

**Evidence** `2db8937` `3444159` `f2487af` `7af16c4` · code-ai-rules.md

### R051 Pure coordinate signatures

**Active · Heuristic**

Pass raw arrays/scalars to coordinate functions, with required arguments. Supply any VM-derived inputs at the boundary instead of accepting RenderVM inside the math.

**Review check and exceptions**

Check parameter types, callees and imports.

**Example** (Git diff `f332b5c` · `components/render/mac/renderAux.swift`)

**Before**

```text
import AppKit

// Converts framebuffer pixels into scene coordinates by removing viewport centering, dividing by display and zoom scale, then adding camera position
func renderCoordFBtoSC(_ position: [Float], _ vm: RenderVM) -> [Float] {
    let scale = renderCoordSCtoFBScale(vm)
    return [
        (position[0] - Float(vm.pixelSize[0]) / 2) / scale[0] + vm.cameraPosition[0],
        (position[1] - Float(vm.pixelSize[1]) / 2) / scale[1] + vm.cameraPosition[1]
```

**After**

```text
import AppKit

// Converts framebuffer pixels into scene coordinates by removing viewport centering, dividing by display and zoom scale, then adding camera position
func renderCoordFBtoSC(_ position: [Float], _ fbSize: [Float], _ cameraPosition: [Float], _ scale: [Float]) -> [Float] {
    return [
        (position[0] - fbSize[0] / 2) / scale[0] + cameraPosition[0],
```

**Evidence** `f332b5c` `2db8937` `05794d1`

### R052 Platform app setup

**Active · Config**

Share Apple setup through HCMAppImpl where possible, keep native hooks in delegates, and mirror Kotlin HCMAppImpl by names, ordering, and retained references. Keep the Activity or App entry point thin.

**Review check and exceptions**

A native context or lifecycle override can remain platform-specific.

For Git Budget's `hello-platform`, link Windows directly to the shared
`HPAppImpl.swift` and generated `dialect.swift` in the macOS target. The
Windows delegate retains the implementation and calls `impl.setup()` from
the app's `onAppear` path before setting the native window icon. Check that
`HPAppImpl` still creates the component and view model before setup
dispatches the first Dialect event. See R046 and R122.

**Example** (Git diff `ad7b59f` · `hello/hello-color-memory/ver-ios/src/HCMAppDelegate.swift`)

**Before**

```text
import UIKit

final class HCMAppDelegate: NSObject, UIApplicationDelegate {
    let hcm = HCMComponent()
    let hcmVM = HCMVM()
    let render = RenderComponent()
    let renderVM = RenderVM()

    func application(
        _ application: UIApplication,
```

**After**

```text
import UIKit

final class HCMAppDelegate: NSObject, UIApplicationDelegate {
    let impl = HCMAppImpl()

    func application(
        _ application: UIApplication,
```

**Evidence** `b4728c1` `ad7b59f` `7ef30e7` `b09602f` `e84c142` · Git Budget `0bde833`

### R053 Avoid unrelated app rewrites

**Active · Review**

Scope changes to the requested app. Do not retroactively change older apps for an HCM-only choice. Update older consumers when needed to preserve a legitimately shared implementation.

**Review check and exceptions**

Use changed-path checks as prompts, not blockers. The explicit os import change was HCM-only.

**Example** (Git diff `e199cb4` · `hello/hello-color-memory/ver-mac/src/render/renderDraw.swift`)

**Before**

```text
#if canImport(RaylibBridge)
    import RaylibBridge
#endif
```

**After**

```text
#if os(macOS)
    import RaylibBridge
#endif
```

**Evidence** `54c05aa` `472ea45` · Chat · renderDraw #if os only for HCM

## Dialect schema, generation and effects

### R054 Authoritative schema

**Active · Config**

Edit the app's dialect.yml for declared state, transitions and effects. Regenerate the configured Swift and Kotlin outputs instead of maintaining generated definitions by hand.

**Review check and exceptions**

Use drift checks with the pinned generator and review Kotlin conversion. Handle explicit user-directed deferrals without pretending artifacts are current.

**Example** (Workflow · updating state)

**Before**

```text
Edit generated dialect.swift or dialect.kt directly
```

**After**

```text
Edit app/dialect.yml
→ run the pinned generator
→ inspect the Swift and Kotlin output
```

**Evidence** `43986d0` `552dce9` `31059ee`

### R055 Dialect field ordering

**Active · Auto**

Alphabetize declared fields within a context and compare generated F field declarations in ASCII order. Apply the same style to new field lists when the schema has no intentional ABI/layout order.

**Review check and exceptions**

Keep case-sensitive ASCII comparison for F. Check current schemas before autofixing ordered value-struct fields.

**Example** (Git snapshots · selected matching lines `b9ebc0e` · `hello/hello-color-memory/ver-mac/src/dialect.swift`)

**Before**

```text
    public var itemColors = [HCMItemColor]()
…
    public var itemPositions = [HCMItemPosition]()
…
    public var items = [HCMItem]()
…
    public var hiddenItemIds = [Int]()
```

**After**

```text
    public var hiddenItemIds = [Int]()
…
    public var itemColors = [HCMItemColor]()
…
    public var itemPositions = [HCMItemPosition]()
…
    public var items = [HCMItem]()
```

**Evidence** `6098939` `c0d85ef` `b9ebc0e` · codestyle.md

### R056 Dialect should ordering

**Active · Auto**

Sort should groups by the field they produce. Keep numbered branch order within each should, since the first matching branch wins.

**Review check and exceptions**

Sort keys in the mapped shoulds block, not label text.

**Example** (Git snapshots · selected matching lines `c0d85ef` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
        selectedItemId:
…
        selectionPair:
…
        isPairMatching:
…
        willDeselectFirstItem:
```

**After**

```text
        isPairMatching:
…
        selectedItemId:
…
        selectionPair:
…
        willDeselectFirstItem:
```

**Evidence** `6098939` `c0d85ef` `e275c18`

### R057 Dialect oneliner ordering

**Active · Auto**

Sort oneliners by field, group duplicate-field effects and preserve their relative order.

**Review check and exceptions**

A stable sort preserves effects such as forwarding matched IDs before feedback or animation.

**Example** (Git snapshots · selected matching lines `e275c18` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
        - itemColors: renderSet(F.itemColors, c.itemColors)
…
        - matchedItemIds: renderSet(F.matchedItemIds, c.matchedItemIds)
…
        - matchedItemIds: hcmAnimateDisappearance(c.matchedItemIds)
…
        - itemColors: RenderVM.shared!.itemColors = c.itemColors
…
        - matchedItemIds: RenderVM.shared!.matchedItemIds = c.matchedItemIds
```

**After**

```text
        - hiddenItemIds: renderSet(F.hiddenItemIds, c.hiddenItemIds)
…
        - itemColors: renderSet(F.itemColors, c.itemColors)
…
        - matchedItemIds: renderSet(F.matchedItemIds, c.matchedItemIds)
…
        - matchedItemIds: hcmAnimateDisappearance(c.matchedItemIds)
…
        - hiddenItemIds: RenderVM.shared!.hiddenItemIds = c.hiddenItemIds
…
        - itemColors: RenderVM.shared!.itemColors = c.itemColors
…
        - matchedItemIds: RenderVM.shared!.matchedItemIds = c.matchedItemIds
```

**Evidence** `6098939` `e275c18` `9b07ff5`

### R058 Branch labels

**Active · Auto**

Terminate every numbered should label with a colon and make it describe the triggering condition. Example: Upon render view resizing.

**Review check and exceptions**

Detect malformed labels because the vendored parser removes the last character.

**Example** (Git diff `dbbc0d0` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
            c.didLaunch = true
items:
    1. Upon measuring the render view:
        if:
            c.recentField == F.uiSize
```

**After**

```text
            c.didLaunch = true
items:
    1. Upon render view resizing:
        if:
            c.recentField == F.uiSize
```

**Evidence** `5884496` `dbbc0d0` · codestyle.md

### R059 Dialect compound conditions

**Active · Auto**

Give each compound if condition its own indented line with `&&` or `||` trailing on the preceding line.

**Review check and exceptions**

Use Dialect's actual syntax, rather than a generic YAML formatter that may rewrite these blocks.

**Example** (Git diff `5884496` · `hello/hello-selection/dialect.yml`)

**Before**

```text
1. Only once during the first setup:
    if:
        c.recentField == F.didSetup && !c.didLaunch
    then:
        c.didLaunch = true
```

**After**

```text
1. Only once during the first setup:
    if:
        c.recentField == F.didSetup &&
        !c.didLaunch
    then:
        c.didLaunch = true
```

**Evidence** `5884496`

### R060 One field per should

**Active · Config**

Each should branch changes the one field named by its group. Emit another rule/effect for a second transition.

**Review check and exceptions**

Check assignment targets, permitting += for the designated field. Branch helpers can compute an assigned value.

**Example** (Illustrative · one target per should)

**Before**

```text
shoulds:
    isGameOver:
        1. Upon matching all tiles:
            then:
                c.isGameOver = true
                c.isRestartVisible = true
```

**After**

```text
shoulds:
    isGameOver:
        1. Upon matching all tiles:
            then:
                c.isGameOver = true
    isRestartVisible:
        1. Upon game over or restart:
            if:
                c.recentField == F.isGameOver
            then:
                c.isRestartVisible = c.isGameOver
```

**Evidence** `36c227d` `56d2959` `e275c18` · components-over-platforms.md

### R061 Required VM arguments

**Active · Config**

Declare required VM arguments and pass RenderVM.shared! explicitly in Swift YAML oneliners. The generator supplies RenderVM.shared!! in Kotlin.

**Review check and exceptions**

Flag a shared singleton default at helper declaration and missing required call arguments.

**Example** (Git diff `ec48880` · `hello/hello-selection/dialect.yml`)

**Before**

```text
- cameraZoom: RenderVM.shared!.cameraZoom = c.cameraZoom
- didPrepareGL: renderInit()
- didPrepareGL: RenderVM.shared!.didPrepareGL = true
```

**After**

```text
- cameraZoom: RenderVM.shared!.cameraZoom = c.cameraZoom
- didPrepareGL: renderInit(RenderVM.shared!)
- didPrepareGL: RenderVM.shared!.didPrepareGL = true
```

**Evidence** `ec48880` `c74e61b`

### R062 Direct VM assignments

**Active · Heuristic**

A single field copy such as HSTVM.shared!.title = c.title goes straight in the oneliner. Avoid forwarding wrappers and artificial indirection for the transpiler.

**Review check and exceptions**

True effects and a required thread hop justify a helper.

**Example** (Git diff `9baa298` · `hello/hello-selection/dialect.yml`)

**Before**

```text
    - selectedItemId: renderSet(F.selectedItemId, c.selectedItemId)
    - title: hstUpdateTitle(c.title)
shoulds:
```

**After**

```text
    - selectedItemId: renderSet(F.selectedItemId, c.selectedItemId)
    - title: HSTVM.shared!.title = c.title
shoulds:
```

**Evidence** `fd524e2` `9baa298` `ec48880` `0b17911`

### R063 Model writes vs native state

**Active · Config**

Native callbacks report lifecycle and input; YAML owns state it declares and effects publish VM copies. Do not add handwritten resets or guards for the same field as routine safety.

**Review check and exceptions**

Current Android didPrepareGL VM writes inside explicit queued init/deinit effects ensure correct GL lifecycle. Treat as a narrow synchronization exception, not a second context owner.

**Example** (Git diff `43986d0` · `components/render/android/renderEffect.kt`)

**Before**

```text
fun renderDeinit(vm: RenderVM) {
    if (!vm.didPrepareGL) return
    raybriShutdown()
    vm.didPrepareGL = false
}

fun renderInit(vm: RenderVM) {
```

**After**

```text
fun renderDeinit(vm: RenderVM) {
    raybriShutdown()
}

fun renderInit(vm: RenderVM) {
```

**Evidence** `9c2041e` `43986d0` `0b17911` `56174f6` `eb3f5ae`

### R064 Preserve event semantics

**Active · Heuristic**

Do not treat true flags as requiring a false→true transition. Dialect fires by recentField, and an event may dispatch the same value repeatedly. Keep platform preparation events distinct if their native preconditions differ.

**Review check and exceptions**

Add an execution check for repeated events and once-only launch. Spot-check generated effects and registration order.

**Example** (Illustrative · repeatable input event)

**Before**

```text
if !didClickIncrement {
    hpSet(F.didClickIncrement, true)
}
```

**After**

```text
hpSet(F.didClickIncrement, true)
// On every click, the rule handles F.didClickIncrement
```

**Evidence** `36c227d` `43986d0` `527efac` · components-over-platforms.md

### R065 Separate mouse and touch rules

**Scoped · Config**

Report mouseDownPos and touchDownPos separately, then normalize into pointerDownPos with separate should branches where each source requires adaptation.

**Review check and exceptions**

Do not collapse them into one compound branch just to shorten the YAML.

**Example** (Git diff `180dd63` · `hello/hello-selection/dialect.yml`)

**Before**

```text
        if:
            c.recentField == F.backingPixelSize
        then:
            c.pixelSize = c.backingPixelSize
pointerDownPos:
    1. Upon mouse or touch down:
        if:
            c.recentField == F.mouseDownPos ||
            c.recentField == F.touchDownPos
        then:
            let position = c.recentField == F.touchDownPos ? c.touchDownPos : c.mouseDownPos
```

**After**

```text
        if:
            c.recentField == F.backingPixelSize
        then:
            c.pixelSize = c.backingPixelSize
pointerDownPos:
    1. Upon mouse down:
        if:
            c.recentField == F.mouseDownPos
        then:
            c.pointerDownPos = renderCoordUItoSC(
                c.mouseDownPos,
                renderUISize(RenderVM.shared!.glView),
                c.pixelSize,
                c.cameraPosition,
                c.cameraZoom
            )
    2. Upon touch down:
        if:
            c.recentField == F.touchDownPos
        then:
```

**Evidence** `2df3640` `180dd63` `05794d1`

### R066 Size publication through Render

**Scoped · Config**

A pixelSize event calls renderResetUISize with Render's VM. Render measures its view, publishes F.uiSize, and forwards uiSize to HCM through the Dialect handoff.

**Review check and exceptions**

Do not derive layout directly from backing pixels or have native resize invoke HCM geometry.

**Example** (Git diff `64dddb7` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
- pointerDownPos: hcmSet(F.pointerDownPos, c.pointerDownPos)
- pixelSize: RenderVM.shared!.pixelSize = c.pixelSize
- pixelSize: hcmSet(F.uiSize, renderUISize(RenderVM.shared!.glView))
- selectedItemId: RenderVM.shared!.selectedItemId = c.selectedItemId
```

**After**

```text
- pointerDownPos: hcmSet(F.pointerDownPos, c.pointerDownPos)
- pixelSize: RenderVM.shared!.pixelSize = c.pixelSize
- pixelSize: renderReportUISize(RenderVM.shared!)
- selectedItemId: RenderVM.shared!.selectedItemId = c.selectedItemId
- uiSize: hcmSet(F.uiSize, c.uiSize)
```

**Evidence** `64dddb7` `3201269` `b81ebbc` `469d231`

### R067 Cross-language literal constants

**Active · Config**

Use one named constant in YAML and define it with each language's representation when inline literals differ. RENDER_PIXEL_SIZE_1x1 is an example.

**Review check and exceptions**

Validate same value and shape, not textual equality between `[1, 1]` and `arrayOf(1, 1)`.

**Example** (Git diff `65e9f1a` · `hello/hello-raylib/dialect.yml`)

**Before**

```text
    then:
        c.pixelSize = renderInitialPixelSize()
2. Upon resize:
```

**After**

```text
    then:
        c.pixelSize = RENDER_PIXEL_SIZE_1x1
2. Upon resize:
```

**Evidence** `65e9f1a` `1522183`

### R068 Shared timing constants

**Scoped · Config**

Use matching identifiers and duration values/units across Swift and Kotlin when equivalent. HCM uses named delay and animation-duration constants, with conversion at APIs that take other units.

**Review check and exceptions**

Validate units as well as numeric equality.

**Example** (Git diff `36b267c` · `components/hcm/hcmConst.swift`)

**Before**

```text
let HCM_ANIMATION_DURATION = 0.3
let HCM_DELAY = 0.3
let HCM_EMPTY_ITEM_IDS = [Int]()
```

**After**

```text
// Define durations in milliseconds
let HCM_ANIMATION_DURATION = 300
let HCM_DELAY = 300
let HCM_EMPTY_ITEM_IDS = [Int]()
```

**Evidence** `0369185` `36b267c` · Chat · unify HCM_DELAY

### R069 Kotlin HCM constant declarations

**Scoped · Auto**

Use val consistently for the targeted Kotlin HCM and Render constants, including arrays. Do not require const val for arrays.

**Review check and exceptions**

The later explicit all-val request supersedes the earlier const-val request for RENDER_CAMERA_POSITION_DEFAULT in the targeted files.

**Example** (Git diff `c5d4450` · `components/hcm/android/hcmConst.kt`)

**Before**

```text
// Define durations in milliseconds
const val HCM_ANIMATION_DURATION = 300
const val HCM_DELAY = 300
val HCM_EMPTY_ITEM_IDS = emptyArray<Int>()
const val HCM_HAPTIC_PULSE_INTERVAL = 80L
```

**After**

```text
// Define durations in milliseconds
val HCM_ANIMATION_DURATION = 300
val HCM_DELAY = 300
val HCM_EMPTY_ITEM_IDS = emptyArray<Int>()
```

**Evidence** `c5d4450` `e7f4cc3` · Chat · Kotlin constants

### R070 Native Swift→Kotlin idioms

**Active · Config**

Use supported source idioms such as Swift .count and .isEmpty and generate Kotlin .size and .isEmpty(). Prefer upstream generator examples and the vendored released script over repo-local wrapper functions or patching generated output.

**Review check and exceptions**

Audit Kotlin syntax, types and rule body after regeneration. Shared YAML used Int rather than a Kotlin-specific Int32 mapping hack.

**Example** (Git diff `31059ee` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
if:
    c.recentField == F.matchedItemIds &&
    otherArrayLength(c.matchedItemIds) == otherArrayLength(c.items) &&
    otherArrayLength(c.items) > 0
then:
    c.isGameOver = true
```

**After**

```text
if:
    c.recentField == F.matchedItemIds &&
    c.matchedItemIds.count == c.items.count &&
    c.items.count > 0
then:
    c.isGameOver = true
```

**Evidence** `336b748` `552dce9` `516ae1e` `31059ee` `81a09d7` · Chat · Int32 and isEmpty requests

### R071 Generator tool preset

**Active · Config**

Copy updated generator-script.swift from sibling Dialect as requested. Verify actual version and 7B model after updating because imported scripts have reverted to 3B. Honor requests to stage or defer execution.

**Review check and exceptions**

Read the checked-in script instead of inferring version/model from documentation.

**Example** (Workflow · generator update)

**Before**

```text
Copy new generator-script.swift
→ run without checking its preset
```

**After**

```text
Copy the requested upstream release
→ verify LLM_DEFAULT_MODEL uses qwen2.5-coder:7b
→ run only when requested
```

**Evidence** `fd524e2` `552dce9` `b91c5a8` `31059ee`

### R072 Generated output topology

**Active · Review**

Generate canonical Swift for macOS and link Apple/mobile and desktop compatible Swift targets to it directly. Kotlin has its own configured output, generated from the same YAML.

**Review check and exceptions**

Validate the exact output paths relative to YAML and compare generated F and effect names across targets.

**Example** (Representative topology · generated sources)

**Before**

```text
ver-mac/src/dialect.swift      # generated
ver-ios/src/dialect.swift      # independent copy
```

**After**

```text
ver-mac/src/dialect.swift      # generated from dialect.yml
ver-ios/src/dialect.swift      → ../../ver-mac/src/dialect.swift
ver-android/…/dialect.kt      # generated from the same YAML
```

**Evidence** `9789b13` `8a60754` `552dce9` · components-over-platforms.md

### R073 Assignment shorthand

**Scoped · Heuristic**

Use += in Dialect when appending a pair to the existing matched IDs, preserving the same update target.

**Review check and exceptions**

Generic += enforcement only where the generator and receiving language implement equivalent collection semantics.

**Example** (Reconstructed from chat · Dialect update)

**Before**

```text
c.matchedItemIds = c.matchedItemIds + c.selectionPair
```

**After**

```text
c.matchedItemIds += c.selectionPair
```

**Evidence** `38c8571` · Chat · matchedItemIds += selectionPair

## Mirroring and native UI

### R074 Working counterpart first

**Active · Review**

Resolve links and inspect the closest working port for each role. Match handler, argument meaning, call order, ownership, failure path, thread and units. Preserve native differences required by the toolkit.

**Review check and exceptions**

Produce a platform call map in review. A similar final screen alone is insufficient.

**Example** (Workflow · adding a native port)

**Before**

```text
Read the requested feature → write a new native handler
```

**After**

```text
Resolve the other port's source → trace native input, Dialect and VM
→ map equivalent calls and lifecycle differences → implement
```

**Evidence** `9789b13` `589c3a3` `2306852`

### R075 No extra defensive machinery

**Active · Heuristic**

Carry over relevant counterpart guards and optional access. Avoid invented guards, fallback branches, duplicate latches, temporary views or wrappers unless the target has a concrete native requirement.

**Review check and exceptions**

Preserve genuine initialization and attachment guards. This is contextual and should not be a blanket ban on guard or if.

**Example** (Git diff `9c2041e` · `components/render/android/renderAux.kt`)

**Before**

```text
package org.opengamestudio

fun renderDraw(vm: RenderVM) {
    if (!vm.didPrepareGL) return
    raybriRender(vm.pixelSize[0], vm.pixelSize[1])
}
```

**After**

```text
package org.opengamestudio

fun renderDraw(vm: RenderVM) {
    raybriRender(vm.pixelSize[0], vm.pixelSize[1])
}
```

**Evidence** `9c2041e` `6a70d0b` `f0b0999` `72679dd` · mirror.md

### R076 Kotlin mirrors Swift structure

**Active · Review**

Preserve Swift-side names, responsibilities, parameter roles, call order, layout, text and visible styling in Kotlin. Use a corresponding native API first, before introducing caches, subclasses or helper layers.

**Review check and exceptions**

Example: renderUISize reads the actual view on both. Android may publish uiSize to a cross-thread VM copy instead of adding a view-level size cache.

**Example** (Git diff `20a5b39` · `components/render/android/renderAux.kt`)

**Before**

```text
fun renderRecreateSurface(vm: RenderVM?) {
    // No-op on Android: GLSurfaceView owns EGL surface recreation
}

fun renderUISize(view: RenderGLView?): Array<Float> {
    return view!!.uiSize
}
```

**After**

```text
fun renderRecreateSurface(vm: RenderVM?) {
    // No-op on Android: GLSurfaceView owns EGL surface recreation
}

fun renderUISize(view: View?): Array<Float> {
    val density = view!!.resources.displayMetrics.density
    return arrayOf(view.width / density, view.height / density)
}
```

**Evidence** `7ef30e7` `20a5b39` `2422bb0` · Chat · generate/mirror Swift into Android

### R077 Declarative UI

**Active · Heuristic**

Keep shared app views expressed in SwiftUI or SwiftCrossUI view builders. Keep raw GTK and WinUI calls in native helpers/adapters. Mirror layout, modifier names, argument labels and order.

**Review check and exceptions**

Compare shared UI ASTs or require the same source link. Do not introduce raw GTK widget indexing to configure a declarative label.

**Example** (Git diff `fa51d88` · `components/hst/linux/view/HSTView.swift`)

**Before**

```text
import GtkBackend
import SwiftCrossUI

struct HSTView: GtkWidgetRepresentable {
    @State var vm: HSTVM

    init(_ vm: HSTVM) {
```

**After**

```text
import SwiftCrossUI

struct HSTView: View {
    @State var vm: HSTVM

    init(_ vm: HSTVM) {
```

**Evidence** `fa51d88` `8623435` `4070b2c` `2306852`

### R078 CUI parity adapters

**Active · Config**

When one toolkit lacks an equivalent modifier, add CUI with the same call syntax. Implement native behavior where needed, or return self in a sanctioned no-op. Keep platform branching in that adapter rather than the UI chain.

**Review check and exceptions**

No-op suitability requires review. Examples include accessibilityHidden and controlSize on Linux.

**Example** (Git diff `c4146bb` · `components/hcm/mac/view/HCMView.swift`)

**Before**

```text
}
    .buttonStyle(.borderedProminent)
    #if !os(Linux)
    .controlSize(.large)
    #endif
```

**After**

```text
}
    .buttonStyle(.borderedProminent)
    .controlSize(.large)
```

**Evidence** `42ce541` `ebec560` `c4146bb` `2306852`

### R079 Observation adapter

**Scoped · Config**

Use `@CUIObservedObject` in the shared HCM and HP views. Apple uses
`ObservedObject` and SwiftCrossUI uses `State`.

**Review check and exceptions**

Choose the real observation wrapper per toolkit. Do not make all models identical by deleting required observation.

For `hello-platform`, check that macOS, iOS, and Windows link directly to
the canonical `components/hp/mac/view/HPView.swift` and
`components/hp/hpVM.swift`. Keep `ObservableObject`, the `@Published`
`countText`, and the weak `HPVM.shared` in one class body. Select Combine
or SwiftCrossUI at import time, and keep the observation type aliases in
CUI. Treat a verified window launch and a verified counter update as
separate results.

**Example** (Representative Swift excerpts · observation parity)

**Before**

```text
#if os(Linux)
    @State var vm: HCMVM
#else
    @ObservedObject var vm: HCMVM
#endif
```

**After**

```text
@CUIObservedObject var vm: HCMVM
// Platform type aliases live in CUI
```

**Evidence** `7b7ad04` `f8f753f` `afcb3c7` · Chat · Combine and State · Git Budget `c0a73df` `f4638c5`

### R080 Desktop size parity

**Scoped · Config**

Use `desktopSize([width, height])` with a shared numeric contract and keep OS-specific application in cuiDesktopSize. iOS can omit desktop sizing.

**Review check and exceptions**

Type-check against minimum deployment OS. Preserve Apple shared App syntax without raw `#if` around a modifier chain.

**Example** (Representative Swift excerpts · window sizing)

**Before**

```text
WindowGroup { /* … */ }
    .defaultSize(width: 640, height: 480)
```

**After**

```text
WindowGroup { /* … */ }
    .desktopSize([640, 480])
```

**Evidence** `00373e1` `42ce541` `6b4668c` `3896685`

### R081 HST title parity

**Scoped · Config**

Use foregroundColor and padding on both SwiftUI and SwiftCrossUI for the HST title. Preserve desired layout alignment and allow clicks through an overlay where requested.

**Review check and exceptions**

Shared API spelling applies to HST's relevant view. Do not assume all future views must prefer foregroundColor to foregroundStyle.

**Example** (Git diff `a8f3534` · `components/hst/mac/view/HSTView.swift`)

**Before**

```text
        .font(.largeTitle)
        .foregroundStyle(.white)
        .offset(x: 1, y: 1)
    Text(vm.title)
        .font(.largeTitle)
        .foregroundStyle(.black)
}
```

**After**

```text
        .font(.largeTitle)
        .foregroundColor(.white)
        .offset(x: 1, y: 1)
    Text(vm.title)
        .font(.largeTitle)
        .foregroundColor(.black)
}
```

**Evidence** `a8f3534` `59ac004` `8623435`

### R082 Hit-testing abstraction

**Scoped · Heuristic**

Use public CUI allowsHitTesting for platform-native wrappers. Apply to the correct wrapped widget rather than `stack.children[1]` or raw GTK calls in HCMView. For dynamic visibility use afterUpdate where creation-only configuration would go stale.

**Review check and exceptions**

Check that the overlay passes clicks and that hidden controls cannot intercept input. Match wrapper ownership, not a brittle child index.

**Example** (Git snapshots · selected matching lines `1ae9a2d` · `components/cui/linux/cuiAllowsHitTesting.swift`)

**Before**

```text
        inspect(.onCreate) { widget in
…
            gtk_widget_set_can_target(widget.parentWidget!.widgetPointer, enabled ? 1 : 0)
```

**After**

```text
        return inspect(.afterUpdate) { widget in
…
            gtk_widget_set_can_target(widget.parentWidget!.widgetPointer, enabled ? 1 : 0)
```

**Evidence** `8623435` `4d6b89a` `0bdff9d` `1ae9a2d` `2306852`

### R083 Named Compose typography

**Scoped · Config**

Prefer MaterialTheme typography over a numeric fontSize for the iOS largeTitle analogue, specifically headlineLarge in the discussed mirror.

**Review check and exceptions**

Other semantic styles remain valid for different text roles.

**Example** (Reconstructed from chat · Compose title)

**Before**

```text
Text(text = vm.title, fontSize = 32.sp)
```

**After**

```text
Text(
    style = MaterialTheme.typography.headlineLarge,
    text = vm.title
)
```

**Evidence** Chat · headlineLarge request · ui.md codestyle.md

### R084 Android safe-area background

**Scoped · Config**

Keep the game-rendering area inside the desired safe area. Fill the region behind system bars with the same blue rather than moving the GL surface just to match its color.

**Review check and exceptions**

Verify display on iOS and Android by screenshot. The blue image and visibility preference are app-specific.

**Example** (Git diff `4e87dac` · `hello/hello-color-memory/ver-android/app/src/main/kotlin/org/opengamestudio/MainActivity.kt`)

**Before**

```text
enableEdgeToEdge(
    navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
    statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
)
```

**After**

```text
enableEdgeToEdge(
    navigationBarStyle = SystemBarStyle.dark(AndroidColor.BLUE),
    statusBarStyle = SystemBarStyle.dark(AndroidColor.BLUE)
)
```

**Evidence** `85cf926` `6684401` `d838bb0` `4e87dac`

### R085 Number roll boundaries

**Scoped · Config**

Keep platform availability and stat transitions inside HCMNumberRollView. Apple can use native numeric text animation. Linux fades only changed text using the platform counterpart. Preserve the initial Waiting label before input.

**Review check and exceptions**

Test initial render and both transition directions. Use a separate native implementation and representable when the stack requires both.

**Example** (Git diff `f8005b7` · `components/hcm/linux/view/HCMNumberRollView.swift`)

**Before**

```text
        .opacity(activeIndex == 1 ? 1 : 0)
}
.animation(animation?.animation, value: activeIndex)
.onChange(of: value) {
    let nextIndex = 1 - activeIndex
    values[nextIndex] = value
    activeIndex = nextIndex
```

**After**

```text
        .opacity(activeIndex == 1 ? 1 : 0)
}
.animation(animation?.animation, value: activeIndex)
.onChange(of: value, initial: true) {
    guard values[activeIndex] != value else { return }

    let nextIndex = 1 - activeIndex
    values[nextIndex] = value
    activeIndex = nextIndex
```

**Evidence** `cb173ac` `cdf2260` `67e3bce` `bc20881` `f8005b7`

### R086 Restart HUD

**Scoped · Config**

Use the standard prominent button style and opacity/animation for visibility. Keep hit testing in sync with visibility and place parity details in CUI. Do not add a one-use alias of vm.isRestartVisible.

**Review check and exceptions**

Validate both visual fade and input behavior. The scope is this HCM control.

**Example** (Git diff `c9c2996` · `components/hcm/mac/view/HCMView.swift`)

**Before**

```text
var body: some View {
    VStack(spacing: 16) {
        HCMNumberRollView(content: statsLabel, value: vm.stats)
            .animation(.easeInOut(duration: HCM_ANIMATION_DURATION), value: vm.stats)
        Spacer()
        if vm.isRestartVisible {
            Button(action: { hcmSet(F.didClickRestart, true) }) {
                Text(l10nConst(L10N_RESTART))
                    .font(.title)
            }
        }
```

**After**

```text
var body: some View {
    VStack(spacing: 16) {
        HCMNumberRollView(content: statsLabel, value: vm.stats)
            .animation(.easeInOut(duration: HCM_ANIMATION_DURATION), value: vm.stats)
        Spacer()
        Button(action: { hcmSet(F.didClickRestart, true) }) {
            Text(l10nConst(L10N_RESTART))
                .font(.title)
        }
            .opacity(vm.isRestartVisible ? 1 : 0)
            .allowsHitTesting(vm.isRestartVisible)
            .accessibilityHidden(!vm.isRestartVisible)
            .animation(.easeInOut(duration: HCM_ANIMATION_DURATION), value: vm.isRestartVisible)
```

**Evidence** `c9c2996` `8d810bb` `ca2fc22` `4070b2c` `645565a`

### R087 Native app entry wiring

**Scoped · Config**

Reuse the counterpart delegate setup/onAppear placement, and use app implementation objects for composition when shared across native entry points. Keep the actual toolkit observation and Combine requirements intact.

**Review check and exceptions**

Verify launch behavior and avoid assuming SwiftUI and SwiftCrossUI wrappers have identical effects.

**Example** (Git diff `5b14b5b` · `hello/hello-selection/ver-linux/src/HSTAppDelegate.swift`)

**Before**

```text
    init() {
        otherSetupConsoleLogging(hst.ctrl, "HST")
        hst.setup()
        otherSetupConsoleLogging(render.ctrl, "Render")
        render.setup()
    }
}
```

**After**

```text
    init() {
        otherSetupConsoleLogging(hst.ctrl, "HST")
        otherSetupConsoleLogging(render.ctrl, "Render")
    }

    func setup() {
        hst.setup()
        render.setup()
    }
}
```

**Evidence** `5b14b5b` `b4728c1` `ad7b59f` `7ef30e7` `f8f753f`

## Rendering, native interop and threading

### R088 UI, FB and SC contracts

**Active · Review**

UI geometry uses local logical units. FB extents are integer pixels and may only be converted to Float where calculations need it. Positions can be fractional. SC uses scene units and camera transforms.

**Review check and exceptions**

Verify UI→FB→SC hit testing agrees with SC→FB rendering for non-square views, scale, resize and zoom.

**Example** (Git diff `076a3de` · `components/render/mac/renderFun.swift`)

**Before**

```text
// Convert a position from framebuffer coordinates into scene coordinates for interpreting pointer input consistently within the currently displayed scene view
func renderCoordFBtoSC(
    _ position: [Float],
    _ fbSize: [Float],
    _ cameraPosition: [Float],
    _ scale: [Float]
) -> [Float] {
```

**After**

```text
// Convert a position from framebuffer coordinates into scene coordinates for interpreting pointer input consistently within the currently displayed scene view
func renderCoordFBtoSC(
    _ position: [Float],
    _ fbSize: [Int],
    _ cameraPosition: [Float],
    _ scale: [Float]
) -> [Float] {
```

**Evidence** `a321d34` `6e44ecd` `f332b5c` `076a3de` `05794d1`

### R089 Framebuffer call boundary

**Scoped · Config**

Accept integer dimensions from target callbacks. Convert size to the C call representation, using a two-element Int32 array in the selected Swift draw sequence.

**Review check and exceptions**

Do not multiply dimensions already provided in pixels by another backing scale.

**Example** (Git diff `1241a04` · `hello/hello-raylib/ver-mac/src/render/renderDraw.swift`)

**Before**

```text
func renderDraw(_ vm: RenderVM) {
    let fbSize = (Int32(vm.pixelSize[0]), Int32(vm.pixelSize[1]))
    raybriRenderEmpty(fbSize.0, fbSize.1)
}
```

**After**

```text
func renderDraw(_ vm: RenderVM) {
    let fbSize = [Int32(vm.pixelSize[0]), Int32(vm.pixelSize[1])]
    raybriRenderEmpty(fbSize[0], fbSize[1])
}
```

**Evidence** `e709d3e` `1241a04` `076a3de`

### R090 Native frame owns context and present

**Active · Config**

Keep make-current and flush/swap outside renderDraw. macOS binds and flushes in its view. iOS/Windows bind and swap around draw. GTK/Android supply a current context and handle presentation.

**Review check and exceptions**

Check call order per platform and teardown context. Do not insert a redundant swap or EGL guard into app drawing.

**Example** (Representative macOS excerpt · binding and presentation)

**Before**

```text
func renderDraw(_ vm: RenderVM) {
    vm.glView.openGLContext?.makeCurrentContext()
    // Draw
    vm.glView.openGLContext?.flushBuffer()
}
```

**After**

```text
// RenderGLView frame callback
openGLContext?.makeCurrentContext()
renderDraw(vm)
openGLContext?.flushBuffer()
```

**Evidence** `d9a153e` `7d801fc` `e6b2ce3`

### R091 App-specific draw pipeline

**Active · Config**

Place each app's actual rendering sequence in its own renderDraw. Projection, clear, background, shapes and flush are explicit calls. A blank app can call raybriRenderEmpty alone.

**Review check and exceptions**

Enforce app ownership and approved operation boundaries. Pipeline order and whether a default clear color belongs in init depend on the app.

**Example** (Representative excerpts · empty and app-specific draw)

**Before**

```text
components/render/mac/renderDraw.swift
    func renderDraw(_ vm: RenderVM) { raybriRender(…) }
```

**After**

```text
hello/hello-raylib/ver-mac/src/render/renderDraw.swift
    func renderDraw(_ vm: RenderVM) { raybriRenderEmpty(…) }

hello/hello-selection/ver-mac/src/render/renderDraw.swift
    // This app supplies its own projection, colors and shapes
```

**Evidence** `d30ba57` `7d801fc` `ae7d211` `00bd868`

### R092 Drawing operation granularity

**Scoped · Config**

Separate 2D framebuffer setup, color, clear, axes and batch flush where independently useful. Use one quad operation when the caller draws individual items instead of a packed N-quad API.

**Review check and exceptions**

Do not generalize “draw one” to ban batching. The helper is raybriDrawQuad with color set independently.

**Example** (Representative call sites · extract drawing operations)

**Before**

```text
raybriRenderQuads(/* packed geometry and origin */)
```

**After**

```text
raybriSetup2DFramebuffer(fbSize[0], fbSize[1])
raybriSetColor(r, g, b, a)
raybriDrawQuad(x, y, width, height)
raybriFlush()
```

**Evidence** `53b9090` `0ce10cb` `2939f19` `5ceca33` `10d591c`

### R093 Bridge compilation model

**Active · Config**

Use one raybri.c compilation entry per target. Include operation headers and enable their implementation guards once. Avoid separate per-operation .c files in this architecture.

**Review check and exceptions**

Validate manifest, source membership, macro ownership and duplicate symbol risk per target. Shared source can be linked from the app's compatible owner.

**Example** (Representative file and macro layout)

**Before**

```text
raybri.c
raybriQuads.c
raybriSetup2DFramebuffer.c
```

**After**

```text
raybri.c:
    #define RAYBRI_SETUP_2D_FRAMEBUFFER_IMPLEMENTATION
    #include "raybriSetup2DFramebuffer.h"
```

**Evidence** `a165fee` `43e7ce5` `f06dc3b`

### R094 Bridge include placement

**Scoped · Config**

Keep `#include` at the top of the header where possible and preserve required order with guards, SDK headers and macros. Avoid exposing unused transitive C types.

**Review check and exceptions**

For Windows test C compilation and Swift importer separately. The latest opaque ANGLE native types supersede the bare windef.h workaround.

**Example** (Representative Windows preprocessor excerpts)

**Before**

```text
#include <windef.h>
#include <EGL/egl.h>
```

**After**

```text
#define EGL_NO_PLATFORM_SPECIFIC_TYPES
#include <EGL/egl.h>
```

**Evidence** `53b9090` `ce2d217` `8ca8b4c` `37b9871` `c6e69f8` · Chat · #include at top

### R095 Minimal blue renderer

**Scoped · Config**

Keep only the rlgl calls required for host-owned OpenGL and blue clear. Comment each retained raylib call where requested. Preserve the trace-level comment and describe rlgl initialization as initializing Raylib's graphics layer with the placeholder 1×1 framebuffer.

**Review check and exceptions**

Scope this to the blue bridge example. Texture/model paths have different setup requirements.

**Example** (Reconstructed from chat · blank bridge initialization)

**Before**

```text
rlglInit(1, 1);
// Additional draw setup for the removed texture/model path
```

**After**

```text
// Initialize Raylib graphics layer with a placeholder 1×1 framebuffer size
rlglInit(1, 1);
```

**Evidence** Chat · OGS Blue raylib review and comments · `43b4848` `d30ba57` `ae7d211`

### R096 OpenGL loader separation

**Scoped · Config**

Separate native extension loading from graphics initialization. Put Android's System.loadLibrary and external loading declaration in raybriAux.kt and call the loader in renderInit before raybriInit with a current context. Mirror native Swift/ES3 helpers.

**Review check and exceptions**

Keep the Android C/JNI bridge a free-function API. Avoid an unused library sentinel or object wrapper.

**Example** (Git diff `9a89001` · `components/raybri/android/raybriAux.kt`)

**Before**

```text
package org.opengamestudio

private val raybriLibrary = System.loadLibrary(RAYBRI_ANDROID_LIB)

external fun raybriLoadExtensions()
```

**After**

```text
package org.opengamestudio

fun raybriLoadExtensions() {
    System.loadLibrary(RAYBRI_ANDROID_LIB)
    raybriLoadExtensionsNative()
}

private external fun raybriLoadExtensionsNative()
```

**Evidence** `8b2698e` `6fb48ec` `e26ff19` `9a89001` `dc2a1df`

### R097 GL logging macros

**Scoped · Config**

Where requested, define platform TRACELOG with a do-while statement macro and route it to the platform logger. Factor repeated JNIEXPORT/Java prefixes into macros.

**Review check and exceptions**

Verify format-string arguments and expansion, but do not patch downloaded Raylib on the basis of a question about its warning.

**Example** (Representative Android JNI excerpts · shared export prefix)

**Before**

```text
JNIEXPORT void JNICALL Java_org_opengamestudio_RaybriKt_raybriInit(JNIEnv *env, jclass cls) {
    raybriInit();
}
```

**After**

```text
#define RAYBRI_JNI(name) JNIEXPORT void JNICALL Java_org_opengamestudio_RaybriKt_raybri##name
RAYBRI_JNI(Init)(JNIEnv *env, jclass cls) {
    raybriInit();
}
```

**Evidence** `13f3c73` `52cfa10` · Chat · iOS/Android TRACELOG requests

### R098 Shared bridge declarations

**Scoped · Config**

Use iOS as the source for identical ES3 Aux declarations used by Android/Windows. Consolidate Glbri's compatible EGL state and default-display path, use the window field, and keep only needed platform includes in glbriConst.h.

**Review check and exceptions**

Keep actual native headers and Windows opaque handle flags. Remove an Aux file whose only helper has become common inline behavior.

**Example** (Git diff `b062218` · `components/glbri/glbriConst.h`)

**Before**

```text
#ifndef GLBRI_CONST_H
#define GLBRI_CONST_H

#if defined(PLATFORM_IOS)
#include "glbriConstIOS.h"
#elif defined(_WIN32)
#include "glbriConstWindows.h"
#endif
```

**After**

```text
#ifndef GLBRI_CONST_H
#define GLBRI_CONST_H

#if defined(PLATFORM_IOS)
#include <libEGL/libEGL.h>
#include <libGLESv2/GLES/gl.h>
#elif defined(_WIN32)
#include <EGL/egl.h>
#endif
```

**Evidence** `dc2a1df` `bb633c7` `290c0ea` `dad1b86` `b678977` `b062218`

### R099 OpenGL version meanings

**Scoped · Config**

Keep the native required context version, the Raylib/rlgl build backend and actual GPU/driver capability separate. macOS and GTK request desktop 3.2 in the inspected samples. The mobile/ANGLE path uses an ES context.

**Review check and exceptions**

Validate platform/profile separately. Do not equate desktop 3.2 with a particular ES number or assume the actual driver advertises exactly the requested minimum.

**Example** (Git diff `925eba8` · `components/render/linux/renderConstLinux.swift`)

**Before**

```text
let RENDER_OPENGL_MAJOR: Int32 = 3
let RENDER_OPENGL_MINOR: Int32 = 3
```

**After**

```text
let RENDER_OPENGL_MAJOR: Int32 = 3
let RENDER_OPENGL_MINOR: Int32 = 2
```

**Evidence** `925eba8` `1ce31de` `837b608`

### R100 Android native view lifetime

**Active · Config**

RenderVM owns glView and Android context where applicable. Delegates initialize and tear down references. Use Activity lifecycle hooks, including onDestroy/tearDown and native onPause, instead of redundant local view ownership.

**Review check and exceptions**

Exercise Activity recreation and check native GL teardown. Do not assume the same view survives all configuration changes.

**Example** (Git snapshots · selected matching lines `1bedff3` · `hello/hello-raylib/ver-android/app/src/main/kotlin/org/opengamestudio/MainActivity.kt`)

**Before**

```text
    private lateinit var glView: RenderGLView
…
        glView = RenderGLView(this)
…
        super.onResume()
…
        glView.onResume()
```

**After**

```text
        RenderVM.shared!!.androidContext = this
…
        super.onResume()
…
        RenderVM.shared!!.glView!!.onResume()
```

**Evidence** `1bedff3` `6e4b51c` `db708d1` `c716ccd` `7ef30e7`

### R101 Lazy Apple view initialization

**Scoped · Config**

Match parameterless RenderGLView() creation and use the lazy field in the merged HCM Apple/desktop Swift RenderVM. Keep shared generated references usable on first access.

**Review check and exceptions**

Confirm constructor conveniences and initialization order. Avoid reintroducing unnecessary startTime.

**Example** (Git diff `24c308b` · `hello/hello-color-memory/ver-mac/src/render/renderVM.swift`)

**Before**

```text
#if os(Windows)
    var frameCallback: EventCleanup?
#endif
#if os(iOS) || os(macOS)
    let glView = RenderGLView()
#else
    // Create the view after GTK or XAML initializes
    lazy var glView = RenderGLView()
#endif
var hiddenItemIds = [Int]()
var itemColors = [HCMItemColor]()
var itemPositions = [HCMItemPosition]()
```

**After**

```text
#if os(Windows)
    var frameCallback: EventCleanup?
#endif
// Defer native view creation until first access
lazy var glView = RenderGLView()
var hiddenItemIds = [Int]()
var itemColors = [HCMItemColor]()
var itemPositions = [HCMItemPosition]()
```

**Evidence** `59e9239` `cf75109` `24c308b`

### R102 Android view/renderer split

**Active · Config**

Keep GLSurfaceView UI lifecycle in RenderGLView on main. Keep native onDrawFrame and renderDraw in RenderGLRenderer on the GL thread. Renderer posts onSurfaceCreated and onSurfaceChanged to the correspondingly named view callbacks.

**Review check and exceptions**

`onSurface*` callbacks originate from GLSurfaceView's GL thread. Calculate/renderSet view size and UI events on main in the final split.

**Example** (Representative Android excerpts · callback split)

**Before**

```text
class RenderGLView(context: Context) :
    GLSurfaceView(context), GLSurfaceView.Renderer {
    // UI and GL callbacks on the same object
}
```

**After**

```text
class RenderGLView(context: Context) : GLSurfaceView(context)

class RenderGLRenderer(private val glView: RenderGLView) : GLSurfaceView.Renderer {
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mainHandler.post { glView.onSurfaceChanged(width, height) }
    }
}
```

**Evidence** `4c993cf` `2422bb0` `9020107` `56a7b7e`

### R103 Android queue boundary

**Active · Config**

Use queueEvent for GL-owned setup and shutdown in the effects elected by Dialect. Keep normal touch, UI publication and game effects on main. Use one preparation effect instead of duplicate oneliners and avoid blocking FutureTask machinery.

**Review check and exceptions**

Keep didPrepareGL visibility and set false once before releasing GL resources. Check pause ordering and context recreation across threads.

**Example** (Git diff `0b17911` · `components/render/android/renderEffect.kt`)

**Before**

```text
        raybriShutdown()
    }
}

fun renderInit(vm: RenderVM) {
    val view = vm.glView!!
    val task = FutureTask {
        raybriLoadExtensions()
        raybriInit()
    }
    view.queueEvent(task)
    // Complete initialization before Dialect publishes didPrepareGL
    task.get()
}
```

**After**

```text
        raybriShutdown()
    }
}

fun renderInit(vm: RenderVM) {
    raybriLoadExtensions()
    raybriInit()
}

fun renderPrepareGL(vm: RenderVM) {
    vm.glView!!.queueEvent {
        renderInit(vm)
        vm.didPrepareGL = true
        renderStartTimer(vm)
    }
}
```

**Evidence** `0b17911` `56174f6` `eb3f5ae` `6a2907e` `4c993cf` · Chat · GL thread iteration

### R104 Android frame scheduling

**Scoped · Config**

Let the start-timer effect choose the renderer's mode. Avoid assigning an initial mode only to override it immediately. Let native GLSurfaceView drive frames and presentation.

**Review check and exceptions**

Do not introduce a duplicate timer on the Android renderer. GTK frame clock and Apple timers are separate native policy.

**Example** (Git diff `6eb587c` · `components/render/android/view/RenderGLView.kt`)

**Before**

```text
    setEGLContextClientVersion(3)
    setEGLConfigChooser(8, 8, 8, 8, 16, 0)
    setRenderer(this)
    renderMode = RENDERMODE_WHEN_DIRTY
}

override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
```

**After**

```text
    setEGLContextClientVersion(3)
    setEGLConfigChooser(8, 8, 8, 8, 16, 0)
    setRenderer(this)
}

override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
```

**Evidence** `6eb587c` `7e32c73` · Chat · leave the mirror

### R105 Shared-thread data

**Active · Heuristic**

Use @Volatile where main writes and GL reads the field, not simply for every VM property. UI-only observed state belongs to the UI framework's own state channel.

**Review check and exceptions**

Atomic publication of a reference is distinct from safety of in-place mutation or consistency across multiple fields. Audit both writers and readers.

**Example** (Illustrative · publication across threads)

**Before**

```text
@Volatile var uiOnlyState = false
var pixelSize = arrayOf<Int>() // Written on main, read on GL
```

**After**

```text
var uiOnlyState = false
@Volatile var pixelSize = arrayOf<Int>()
```

**Evidence** `ba0c6e5` `20a5b39` `4c993cf` · Chat · volatile and size

### R106 GTK native callback identity

**Scoped · Config**

Recover the originating RenderGLView from GTK userdata. Use the existing C-convention callback style for lifecycle and click paths, with shared pointer recovery and signal registration in renderSetupGtkSignals.

**Review check and exceptions**

Do not route a native callback through the singleton if userdata provides the actual view. Where a native closure does capture the view, use the requested weak capture.

**Example** (Git diff `b3d8da9` · `components/render/linux/view/RenderGLView.swift`)

**Before**

```text
gtk_widget_set_hexpand(widgetPointer, 1)
gtk_widget_set_vexpand(widgetPointer, 1)

let realize: @convention(c) (UnsafeMutableRawPointer?, UnsafeMutableRawPointer?) -> Void = { _, _ in
    RenderVM.shared!.glView.onRealize()
}
let render: @convention(c) (UnsafeMutableRawPointer?, OpaquePointer?, UnsafeMutableRawPointer?) -> Int32 = { _, _, _ in
    RenderVM.shared!.glView.onRender()
```

**After**

```text
gtk_widget_set_hexpand(widgetPointer, 1)
gtk_widget_set_vexpand(widgetPointer, 1)

let realize: @convention(c) (UnsafeMutableRawPointer?, UnsafeMutableRawPointer?) -> Void = { _, data in
    let view = Unmanaged<RenderGLView>.fromOpaque(data!).takeUnretainedValue()
    view.onRealize()
}
let render: @convention(c) (UnsafeMutableRawPointer?, OpaquePointer?, UnsafeMutableRawPointer?) -> Int32 = { _, _, _ in
    RenderVM.shared!.glView.onRender()
```

**Evidence** `b3d8da9` `c2d9614` `64beb74` `f153f86` `ecfb63b`

### R107 WinUI input attachment

**Scoped · Config**

Attach the handler directly to SwapChainPanel when supported. Remove the overlay and redundant hit-test initializer parameter. Register input and lifecycle at the same init seam. The selection example uses the left mouse button.

**Review check and exceptions**

Do not implement touch selection merely because the API also supports touch. Native failure/attachment and callback lifetime checks remain case-specific.

**Example** (Representative Swift excerpts · Windows input attachment)

**Before**

```text
canvas.pointerPressed.addHandler { [weak view] _, event in
    view?.onPointerDown(event)
}
```

**After**

```text
view.pointerPressed.addHandler { [weak view] _, event in
    view?.onPointerDown(event)
}
```

**Evidence** `a053fd5` `7bcdb28` `79c9e1d`

## App-specific gameplay and user experience

### R108 HST item identifiers

**Scoped · Runtime**

Use array indices for selection IDs, 0 through 3 in the four-item demo, and -1 for a miss. Delegate selection to HST, with local UI clicks converted to SC before hit testing.

**Review check and exceptions**

Keep a valid miss guard where the user clarified it was necessary. One shared range contract prevents off-by-one errors.

**Example** (Git diff `868fe38` · `components/hst/hstFun.swift`)

**Before**

```text
        y >= item.y && y < item.y + item.height
    }
    return index.map { $0 + 1 } ?? 0
}
```

**After**

```text
        y >= item.y && y < item.y + item.height
    }
    return index ?? -1
}
```

**Evidence** `868fe38` `9230c12` · Chat · keep the -1 check

### R109 HCM board constraints

**Scoped · Runtime**

Display 16 square tiles, with interior gaps and double edge gaps, fitting the available view. Use viewport aspect ratio to choose columns, including a three-column portrait layout and centered partial rows.

**Review check and exceptions**

Derive from geometry without an iOS-only forced column count. Verify minimum size, wide, tall and rotation.

**Example** (Git snapshots · selected matching lines `289d0d7` · `components/hcm/hcmAux.swift`)

**Before**

```text
    let gap = min(25, width / Float(columns * 2), height / Float(rows * 2))
…
    let side = min(
…
        (width - horizontalGaps - 2 * gap) / Float(columns),
…
        (height - verticalGaps - 2 * gap) / Float(rows)
```

**After**

```text
    let gap = min(25, width / Float(2 * (columns + 1)), height / Float(2 * (rows + 1)))
…
    let edgeGap = 2 * gap
…
    let side = min(
…
        (width - horizontalGaps - 2 * edgeGap) / Float(columns),
…
        (height - verticalGaps - 2 * edgeGap) / Float(rows)
```

**Evidence** `9d8b6e3` `804c31d` `18534cf` `289d0d7` `94f148c` `04836cc`

### R110 HCM item and color generation

**Scoped · Runtime**

Generate pairs through HCM, shuffle with standard-library behavior, and derive colors on items generation rather than didLaunch. Use itemPositions separately for geometry on resize.

**Review check and exceptions**

Verify matching pairs share the intended group/color and resizing leaves gameplay items intact.

**Example** (Reconstructed from chat · item color trigger)

**Before**

```text
if:
    c.recentField == F.didLaunch
then:
    c.itemColors = hcmGenerateItemColors(c.items)
```

**After**

```text
if:
    c.recentField == F.items
then:
    c.itemColors = hcmGenerateItemColors(c.items)
```

**Evidence** `dd98dc3` `ee64341` `3f1d089` · Chat · generate colors upon items

### R111 HCM selection queue

**Scoped · Runtime**

Keep the last up-to-two selected IDs in order. When a new ID arrives at capacity, advance from the first selection. Evaluate pair matching in HCM with items first. Render colors all IDs in selectionPair.

**Review check and exceptions**

Test repeated clicks, match, miss, cancellation and fairness. “LILO queue” in the request is grounded by the observed append/drop-oldest behavior.

**Example** (Representative excerpts · selection publication and queue)

**Before**

```text
- selectedItemId: renderSet(F.selectedItemId, c.selectedItemId)
```

**After**

```text
- selectionPair: renderSet(F.selectionPair, c.selectionPair)

// hcmSelectionPair tail
if !pair.contains(selectedItemId) {
    pair.append(selectedItemId)
}
return Array(pair.suffix(2))
```

**Evidence** `ad298e9` `08c75e5` `960d575`

### R112 HCM delayed deselection

**Scoped · Runtime**

Place hcmAnimateDeselection in Effect. Schedule the roughly 0.3 s delay via a named constant and report didDeselectFirstItem. Keep actual deselection as a pure function that drops the first selection.

**Review check and exceptions**

Distinguish intent and completion with event IDs so restart or repeated events do not create a cycle.

**Example** (Representative excerpts · asynchronous completion identity)

**Before**

```text
// hcmAux.swift, early version
hcmSet(F.didDeselectFirstItem, true)
```

**After**

```text
// hcmEffect.swift
DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(HCM_DELAY)) {
    hcmSet(F.didDeselectFirstItem, uuid)
}
```

**Evidence** `960d575` `378c7db` `527efac` `d41de69`

### R113 HCM matched vs hidden

**Scoped · Runtime**

Track matchedItemIds and hiddenItemIds separately so a match remains visible during the short animation. Record one match start time in the Render oneliner, scale the active matched pair to 115%, then hide through the delayed effect.

**Review check and exceptions**

Keep timing in Render, with constants in renderConst and time query in app RenderAux. Do not use per-pair or cross-component VM timing state.

**Example** (Git diff `e275c18` · `hello/hello-color-memory/ver-mac/src/render/renderDraw.swift`)

**Before**

```text
raybriDrawBackgroundCentralRadialGradient(fbSize[0], fbSize[1])
for (id, quad) in vm.itemPositions.enumerated() {
    if vm.matchedItemIds.contains(id) {
        continue
    }
```

**After**

```text
raybriDrawBackgroundCentralRadialGradient(fbSize[0], fbSize[1])
for (id, quad) in vm.itemPositions.enumerated() {
    if vm.hiddenItemIds.contains(id) {
        continue
    }
```

**Evidence** `9b8b240` `e275c18` `753db62` `8429148` `5c7a8ce` `b64c783`

### R114 HCM completion and restart

**Scoped · Runtime**

Derive restart visibility from game-over state. A restart starts fresh items and state without a self-sustaining cycle. Keep a distinct starting stats label and show matched stats after the relevant events.

**Review check and exceptions**

Verify match all, confirm restart visibility using stderr, trigger Restart and verify a playable reset and initial label.

**Example** (Git diff `527efac` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
if:
    c.recentField == F.matchedItemIds &&
    c.matchedItemIds.count == c.items.count
then:
    c.isGameOver = true
```

**After**

```text
if:
    c.recentField == F.matchedItemIds &&
    c.matchedItemIds.count == c.items.count &&
    c.items.count > 0
then:
    c.isGameOver = true
```

**Evidence** `bbd4379` `527efac` `d41de69` `f8005b7` `afcb3c7`

### R115 HCM haptic effect

**Scoped · Runtime**

Use a separate hcmVibrate oneliner on matchedItemIds, passing the list through. Put the empty-list check in the effect. Implement success feedback on iOS and Android and a macOS no-op.

**Review check and exceptions**

Keep vibration separate from disappearance. Android's chosen pattern and intensity are app design settings, not general lint defaults.

**Example** (Git diff `9b07ff5` · `hello/hello-color-memory/dialect.yml`)

**Before**

```text
- items: renderSet(F.items, c.items)
- matchedItemIds: renderSet(F.matchedItemIds, c.matchedItemIds)
- matchedItemIds: if !c.matchedItemIds.isEmpty { hcmVibrate() }
- matchedItemIds: hcmAnimateDisappearance(c.matchedItemIds)
- selectionPair: renderSet(F.selectionPair, c.selectionPair)
```

**After**

```text
- items: renderSet(F.items, c.items)
- matchedItemIds: renderSet(F.matchedItemIds, c.matchedItemIds)
- matchedItemIds: hcmVibrate(c.matchedItemIds)
- matchedItemIds: hcmAnimateDisappearance(c.matchedItemIds)
- selectionPair: renderSet(F.selectionPair, c.selectionPair)
```

**Evidence** `7ce81d2` `9b07ff5` `cca2261` `b7cc87b` `9f92005`

### R116 HCM radial background

**Scoped · Runtime**

Clear the scene, draw the centered radial gradient, then draw tiles and flush. Preserve blue corners and a visibly brighter center.

**Review check and exceptions**

Keep this app's background distinct from shared bridge initialization. Visual checks are needed.

**Example** (Git diff `229c6e6` · `hello/hello-color-memory/ver-mac/src/render/renderDraw.swift`)

**Before**

```text
raybriSetup2DFramebuffer(fbSize[0], fbSize[1])
raybriClear(0, 0, 255, 255)
raybriDrawBackgroundGradient(fbSize[0], fbSize[1])
for (id, quad) in vm.itemPositions.enumerated() {
    let point = renderCoordSCtoFB([quad.x, quad.y], vm.pixelSize, vm.cameraPosition, scale)
```

**After**

```text
raybriSetup2DFramebuffer(fbSize[0], fbSize[1])
raybriClear(0, 0, 255, 255)
raybriDrawBackgroundCentralRadialGradient(fbSize[0], fbSize[1])
for (id, quad) in vm.itemPositions.enumerated() {
    let point = renderCoordSCtoFB([quad.x, quad.y], vm.pixelSize, vm.cameraPosition, scale)
```

**Evidence** `00bd868` `6de9b18` `229c6e6`

## Build, dependency and packaging conventions

### R117 Shared Bash entry points

**Active · Config**

Use Bash, including Git Bash for Windows, with build/run entry points and sourced util/step scripts. Keep SDIR anchored to the physical entry directory with pwd -P. Use TARGET and STEP and the established numbered echo format for run and build steps.

**Review check and exceptions**

Do not re-anchor a sourced step to its apparent path. Preserve shell quoting and platform tooling.

**Example** (Reconstructed from chat · standardized Bash steps)

**Before**

```text
swift build --package-path ver-windows
```

**After**

```text
SDIR=$(cd "$(dirname "$0")" && pwd -P)
T=$SDIR/step
TARGET=hello-platform
STEP=0
source "$T/buildWindows" "$TARGET"
```

**Evidence** `6a49531` `c8d1ecf` `871d6ed` · Chat · Git Bash and echo

### R118 Build vs run

**Active · Config**

Keep application launch separate from building. Launch existing products, with the app icon through the normal desktop launcher where required. Avoid redundant build-first banners, exec or cygpath unless a concrete need is shown.

**Review check and exceptions**

Do not take absence of exec as general Bash style. This is the established desktop script pattern.

For Git Budget's `hello-platform`, build macOS and Windows with
`swift build --configuration release`. Check that macOS packaging reads
the release binary and that the Windows launcher opens the release
executable. The Windows build runs `llvm-strip --strip-debug` after
linking. Inspect the final PE for remaining DWARF sections, DLL imports,
and the icon resource, then check that the stripped executable opens on
Windows. When reviewing a change to size, measure the executable and
the full distribution payload separately. Use the counting method in
the [Windows verification record](#git-budget-windows-verification-record).

**Example** (Git Budget · Windows run step)

**Before**

```text
APP=$(cygpath -w "$BIN/hello-platform.exe")
exec "$APP"
```

**After**

```text
cd "$SDIR/../ver-windows"
"./.build/release/$TARGET.exe"
```

**Evidence** `6a49531` · Chat · run-windows, cygpath and exec corrections · Git Budget `c0a73df`

### R119 Linux packaging stage

**Scoped · Config**

Call packageLinux from build-linux. The packaging step creates the directories, copies the existing binary, icon and desktop template, and updates caches. Avoid additional OS checks, rebuilds and Python in that step.

**Review check and exceptions**

Use mkdir -p and cp rather than install with explicit 644/755 for these known inputs. Preserve executable source mode. Call GTK/Desktop update tools directly.

**Example** (Reconstructed from chat · Linux package setup)

**Before**

```text
install -d "$BIN_DIR"
install -m 755 "$APP_DIR/.build/debug/$TARGET" "$BIN_DIR/$TARGET"
```

**After**

```text
mkdir -p "$BIN_DIR"
cp "$APP_DIR/.build/debug/$TARGET" "$BIN_DIR/$TARGET"
```

**Evidence** `6a49531` `775fd57` · Chat · install-linux simplification

### R120 Linux launcher template

**Scoped · Config**

Copy res/linux-app.desktop. Substitute %APP_NAME%, %APP_EXEC% and %APP_ID% by sed. Keep reusable template placeholders instead of hard-coding hello-platform. Use the graphical launch path for icon integration.

**Review check and exceptions**

Check source filename, placeholder coverage, installed application ID and absolute Exec path.

**Example** (Reconstructed from chat · desktop launcher template)

**Before**

```text
Name=hello-platform
Exec=hello-platform
Icon=org.opengamestudio.hello-platform
```

**After**

```text
Name=%APP_NAME%
Exec=%APP_EXEC%
Icon=%APP_ID%
```

**Evidence** `6a49531` `b111ab6` · Chat · template and gtk-launch requests

### R121 Windows icon linking

**Scoped · Config**

Use the precompiled AppIcon.res, referenced from APP_RES_DIR derived from SDIR. Avoid regenerating or copying the icon, compiling the resource on each build, and keeping an unused duplicate .ico. Use manifest force unwrap for required resource input and interpolate inline where requested.

**Review check and exceptions**

Do not add .when(platforms:) around Windows-only manifest settings just to identify Windows. Keep app resource ID in sync with the compiled resource.

**Example** (Reconstructed from chat · Windows manifest resource)

**Before**

```text
let iconResource = URL(fileURLWithPath: #filePath)
    .deletingLastPathComponent().appendingPathComponent(".build/AppIcon.res").path
```

**After**

```text
let resDir = ProcessInfo.processInfo.environment["APP_RES_DIR"]!
// The linker flags use "\(resDir)/AppIcon.res" directly
```

**Evidence** `6a49531` · Chat · icon resource, force unwrap and SDIR

### R122 Windows window icon lifecycle

**Scoped · Review**

Put native title/window icon setup in delegate setup called via onAppear once the native window exists. Preserve the resource group ID (101 in this resource) and current UI-thread lookup.

**Review check and exceptions**

Avoid making the ID arbitrary on the Swift side. Decode .res or inspect Win32 linkage before enforcing. Match WindowsBool and pointer-sized types at the callback boundary.

**Example** (Reconstructed from chat · SwiftCrossUI window setup)

**Before**

```text
// Install the resource for the executable only
```

**After**

```text
.onAppear {
    delegate.setup()
}
// Delegate sets both native window icons using group ID 101
```

**Evidence** `6a49531` · Chat · title icon, LPARAM and delegate

### R123 macOS/Linux dependency parity

**Scoped · Config**

Use pkg-config and system-library target src/craylib. Share shim.h and use platform module maps beneath components/craylib for differing linkage. Keep explicit Linux GL linkage where required.

**Review check and exceptions**

Validate both manifests and actual installed raylib.pc and rlgl.h. Do not restore RAYLIB_PREFIX for this path.

**Example** (Representative manifest excerpts · Raylib discovery)

**Before**

```text
let raylibPrefix = ProcessInfo.processInfo.environment["RAYLIB_PREFIX"]!
```

**After**

```text
.systemLibrary(name: "CRaylib", path: "src/craylib", pkgConfig: "raylib")
```

**Evidence** `b11de8e` `711b41a` `d8df00c` `837b608`

### R124 Android Raylib version handoff

**Scoped · Config**

Use the Gradle TOML catalog key raylib with a public release tag, introduced as 6.2. Pass it via RAYLIB_VERSION to CMake FetchContent for rlgl.h. Avoid checked-in duplicate rlgl headers and raw revision names.

**Review check and exceptions**

CMake minimum 3.11 reflects FetchContent. Selected Android tool 3.22.1 is separate. Ignore .cxx build artifacts.

**Example** (Representative build excerpts · shared official Raylib tag)

**Before**

```text
# libs.versions.toml
raylibRevision = "<git commit>"
```

**After**

```text
# libs.versions.toml
raylib = "6.2"

# build.gradle.kts
arguments += "-DRAYLIB_VERSION=${libs.versions.raylib.get()}"
```

**Evidence** `55707cc` `67d5c41` `5751f4a` `d5c4e8a` `05f9061`

### R125 Windows dependency pinning

**Scoped · Config**

Pin the WinUI version with exact SwiftPM version syntax. A numeric release tag is not a branch. Avoid `upToNext*` for the fixed dependency.

**Review check and exceptions**

Check that the target's required product list and package match the version. Do not autofix dependency ranges elsewhere.

Git Budget's Windows `hello-platform` pins SwiftCrossUI exactly and
uses its `SwiftCrossUI` and `WinUIBackend` products. Record the resolved
transitive SwiftWinUI version when comparing builds.

**Example** (Git diff `e711634` · `hello/hello-raylib/ver-windows/Package.swift`)

**Before**

```text
    .package(url: "https://github.com/moreSwift/swift-cross-ui", branch: "v0.10.0"),
    .package(url: "https://github.com/moreSwift/swift-winui", branch: "0.2.2")
],
```

**After**

```text
    .package(url: "https://github.com/moreSwift/swift-cross-ui", branch: "v0.10.0"),
    .package(url: "https://github.com/moreSwift/swift-winui", exact: "0.2.2")
],
```

**Evidence** `11516d9` `1212d1b` `e711634` `db8c289` · Git Budget `0bde833`

### R126 Windows ANGLE build staging

**Scoped · Config**

Resolve local sibling angle, raylib and skiasharp from entry-script SDIR. Prepare import libraries under .build/angle, build, then copy only the runtime ANGLE DLLs to the output using the matching target architecture.

**Review check and exceptions**

Name the DLL copying step copyWindowsANGLE. No separate prepare-windows entry point, downloader, Darwin branch, ANGLE_DIR override or incidental license-copy step in this local build workflow. Do not generalize this to license obligations.

**Example** (Representative Bash step lists · Windows ANGLE)

**Before**

```text
util/prepare-windows
util/build-windows
```

**After**

```text
# util/build-windows
source "$T/prepareWindowsANGLE" "$TARGET"
source "$T/buildWindows" "$TARGET"
source "$T/copyWindowsANGLE" "$TARGET"
```

**Evidence** `f4100d7` `b98bff2` `ba2731e` `871d6ed` `f9b251d`

### R127 Windows bridge dependency

**Scoped · Config**

Initial UI-only dependency stage may declare CRaylib with no calls. Once RaylibBridge compiles the Windows ES3 bridge, remove the redundant Windows CRaylib target.

**Review check and exceptions**

Keep the macOS/Linux CRaylib system-library paths where needed. Use a stage-aware expectation.

**Example** (Representative SwiftPM arrangement · Windows dependency)

**Before**

```text
Executable → RaylibBridge → CRaylib system-library target
```

**After**

```text
Executable → RaylibBridge (compiles standalone rlgl)
```

**Evidence** `ff00431` `6d46961` `cb0aa33` `ce2d217`

### R128 Apple and Android target assets

**Scoped · Config**

Link identical icons, Gradle wrapper files and gradlew to a canonical copy. Use a large PNG as the Android adaptive icon source where requested. Track aspect and runtime metadata separately.

**Review check and exceptions**

Validate Git link mode and target packaging. Android minSdk 23 and the OGS orientation change are app settings, not global constraints.

**Example** (Git snapshots · selected matching lines `9ca57de` · `hello/hello-raylib/ver-android/gradlew`)

**Before**

```text
#!/bin/sh
```

**After**

```text
../../hello-platform/ver-android/gradlew
```

**Evidence** `cc99597` `9ca57de` · Chat · native icon and SDK corrections

### R129 Platform-specific bridge declarations

**Scoped · Config**

Use the target-specific build and include paths under src/raybri or the matching Android C subdirectory, even when the implementation file is shared. For HCM renderDraw use an explicit OS conditional to import RaylibBridge on the targets that import it.

**Review check and exceptions**

Keep iOS bridging-header use where configured. Do not convert older app imports just because HCM chose explicit os conditions.

**Example** (Representative Swift excerpts · HCM only)

**Before**

```text
#if canImport(RaylibBridge)
    import RaylibBridge
#endif
```

**After**

```text
#if os(Linux) || os(macOS) || os(Windows)
    import RaylibBridge
#endif
```

**Evidence** `52cfa10` `54c05aa` `1bdf0de` `db8c289` · Chat · HCM import only

## Review requirements and pre-import decisions

### R130 Requirements for platform stubs

**Active · CI**

Study prior ports, preserve API parity, build and run the target, exercise representative behavior, inspect stderr, then audit the actual result against prompt intent and repository conventions.

**Review check and exceptions**

Record source review, successful compilation, observed UI and log-confirmed logic as separate evidence. Context-specific user directions can limit target validation.

**Example** (Workflow · platform stub checklist)

**Before**

```text
Copy view → compile → hand off
```

**After**

```text
Study prior ports → preserve API parity
→ build and launch → exercise behavior and inspect stderr
→ review against prompt intent and repository rules
```

**Evidence** `9789b13` `589c3a3` `afcb3c7` `72d2a8f` · AGENTS.md

### R131 Verification at the real consumer

**Active · CI**

Verify generated output, actual symlinks, C and Swift importer builds and native behavior separately. For HCM ports, use the platform app window for interactive verification when requested and logs for selection, completion and restart.

**Review check and exceptions**

Skip native builds/generation when the user explicitly requests source only. Do not report successful runtime from a manifest parse or screenshot.

For Windows, use a native target of the intended architecture. SSH can
run the build and inspect the executable. Verify a WinUI launch in the
signed-in Windows desktop session and record any separately exercised
behavior. If a VM drops SSH during compilation, check the host's storage
and hypervisor log before attributing the interruption to the build.

**Example** (Workflow · separate verification evidence)

**Before**

```text
"Manifest parses, so the port works"
```

**After**

```text
C translation unit compiled
Swift import and app build succeeded
Native click observed in the app window
stderr confirms selection, completion and restart
```

**Evidence** `1212d1b` `37b9871` `afcb3c7` · Chat · use app windows only · Git Budget Windows verification, 2026-11-27

### R132 Incremental stubs

**Active · Review**

Keep requested stages separate. A UI stub, native dependency, bridge, and render connection can be individually reviewed. A functional clone needs matching renderer, input and behavior.

**Review check and exceptions**

Do not claim stage-specific omissions as global exclusions. Follow user-controlled review checkpoints and rollbacks.

**Example** (Workflow · staged Windows port)

**Before**

```text
One draft adds the UI, downloads dependencies and connects rendering
```

**After**

```text
Stage 1: UI, scripts and icon
Stage 2: dependency only
Stage 3: native bridge
Stage 4: connect rendering
```

**Evidence** `d66ceca` `e77800c` `ff00431` `6d46961` `cb0aa33` `e6b2ce3` `bf52dd9`

### R133 Avoid extra artifacts

**Active · Review**

Keep sample-level README files and persistent tests out of changes unless requested, required, or needed to capture a specific regression.

**Review check and exceptions**

This is a review policy with exceptions, not a ban on tests or shared documentation.

**Example** (Representative tree change · unsolicited artifacts)

**Before**

```text
hello/hello-raylib/README.md
hello/hello-selection/ver-mac/Tests/
```

**After**

```text
Use the existing repository README and app guide
Use focused verification for the routine selection change
```

**Evidence** `f5ef941` `c2e4fa2`

### R134 Post-stub intent review

**Active · Review**

After generating and verifying the stub, compare the diff back to the user's stated intent. Resolve avoidable API mismatches in that pass.

**Review check and exceptions**

Inspect code structure, not just behavior. An explicit CUI no-op or native implementation belongs in the parity decision.

**Example** (Workflow · post-stub API review)

**Before**

```text
The application renders its expected screen
```

**After**

```text
Compare each shared HCMView modifier and call signature
Move backend details into CUI
Recheck runtime behavior and report remaining deviations
```

**Evidence** `2306852` `afcb3c7` `72d2a8f` · Chat · review your stub

### R135 OGS Blue constant and GL naming

**Pre-import · Config**

Move the macOS GL dlopen path to raybriConst.h as RAYBRI_MAC_GL. Keep appropriate internal linkage for a C header constant. State the purpose and placeholder framebuffer size beside rlglInit.

**Review check and exceptions**

Scope to ogs-blue history in chat. Do not enforce old linking paths on hello-raylib.

**Example** (Reconstructed from chat · macOS bridge constant)

**Before**

```text
// raybri.c
dlopen("/System/Library/Frameworks/OpenGL.framework/OpenGL", …);
```

**After**

```text
// raybriConst.h
static const char *const RAYBRI_MAC_GL =
    "/System/Library/Frameworks/OpenGL.framework/OpenGL";
// raybri.c
dlopen(RAYBRI_MAC_GL, …);
```

**Evidence** Chat · RAYBRI_MAC_GL, static const and rlglInit

### R136 OGS Blue bridge and app structure

**Pre-import · Config**

Place direct drawing calls in the matching Render Swift files, reference Blue.c in the macOS target, and avoid an EGL include in raybri.h when the header's public interface and real builds do not need it.

**Review check and exceptions**

These are app decisions discussed before this Git history. Verify modern consumers before removing any include.

**Example** (Reconstructed from chat · unused public header dependency)

**Before**

```text
// raybri.h
#if defined(PLATFORM_IOS)
#include "../egl/egl.h"
#endif
```

**After**

```text
// raybri.h
// Public Raybri declarations omit the unused EGL dependency
// Apple Render view and effect files call the bridge
```

**Evidence** Chat · Blue.c and conditional egl.h

### R137 OGS Blue iOS and Android stubs

**Pre-import · Config**

Enable landscape in the requested iOS plist. Android starts as a UI-only Blue application, uses a delegate object for shared component and logging, and mirrors Swift UI naming and layout. The generated Kotlin component is an object.

**Review check and exceptions**

Scope to that app. OGS Blue and the existing HCMAppImpl are different names and historical stages.

**Example** (Reconstructed from chat · Android component wiring)

**Before**

```text
RootView()
```

**After**

```text
// Delegate owns the generated Blue component object
object ActivityDelegate {
    val blue = BlueComponent
}
BlueView()
```

**Evidence** Chat · iOS orientation, BlueView and ActivityDelegate

### R138 Superseded macOS Raylib paths

**Historical · Review**

Early manifests force-unwrapped RAYLIB_PREFIX and bridge sections were temporarily split into C translation units. Later pkg-config and single-entry raybri.c changes replace these rules.

**Review check and exceptions**

Keep as migration evidence. Do not lint for the old path or multiple entry points.

**Example** (Representative historical migration · macOS dependencies)

**Before**

```text
let raylibPrefix = ProcessInfo.processInfo.environment["RAYLIB_PREFIX"]!
```

**After**

```text
// Discover the installed headers and library through pkg-config
.systemLibrary(name: "CRaylib", path: "src/craylib", pkgConfig: "raylib")
```

**Evidence** `fbbad44` `b11de8e` `a165fee` `43e7ce5`

### R139 Superseded Windows preparation variants

**Historical · Review**

Separate prepare/download scripts, automatic downloads, a provisional CRaylib target and explicit windef.h ordering were intermediate solutions. The final bridge favors sibling deps, its own native bridge and opaque EGL handles.

**Review check and exceptions**

Check the commit at the branch tip before deriving a dependency rule from a vendor log.

**Example** (Representative historical migration · Windows dependencies)

**Before**

```text
downloadWindowsANGLE → prepare-windows → build-windows
RaylibBridge → provisional CRaylib
```

**After**

```text
build-windows → prepare from sibling dirs → build → copy DLLs
RaylibBridge compiles rlgl directly
```

**Evidence** `35c12fe` `7423c6a` `ba2731e` `871d6ed` `ce2d217` `ba2867e` `37b9871`

### R140 Superseded Android threading variants

**Historical · Review**

Earlier trials queued touch and all effects, combined UI and GL roles, or introduced didInitGL/FutureTask. The retained design separates view and renderer and queues GL resource effects.

**Review check and exceptions**

Do not enforce transient requests for renderPixelSize to run on GL after the later explicit move to GLView/main.

**Example** (Representative historical migration · Android touch dispatch)

**Before**

```text
queueEvent {
    renderSet(F.touchDownPos, position)
}
```

**After**

```text
// RenderGLView touch callback is already on main
renderSet(F.touchDownPos, position)
// GL init/deinit alone use queueEvent in the effects
```

**Evidence** `a2387be` `98100e8` `377e42f` `e18c67a` `4c993cf` `2422bb0` `9020107`

### R141 Superseded HCM UI and renderer variants

**Historical · Review**

Forced iOS columns, a raw GTK HUD, duplicated control branches, broad renderAux relocation and per-pair animation state were reviewed or replaced. Prefer geometry, CUI parity, retained owner links and one match clock.

**Review check and exceptions**

Distinguish these from valid exploratory commits and app behavior invariants.

**Example** (Representative historical migration · HCM layout)

**Before**

```text
#if os(iOS)
    return 3
#endif
```

**After**

```text
if height >= width * 1.5 {
    return 3
}
```

**Evidence** `94f148c` `04836cc` `f215b12` `9bfe90b` `8429148` `ca2fc22` `4070b2c` `c4146bb`

### R142 Project metadata scope

**Scoped · Config**

Keep each target's name and app identity close to its working counterpart. Preserve platform deployment requirements explicitly and keep changes local to the app when directed.

**Review check and exceptions**

Check plist and Gradle/manifest ownership. Do not infer that a chosen SDK minimum or supported orientation is a repo-wide law.

**Example** (Illustrative · app-specific Android deployment setting)

**Before**

```text
// Apply minSdk 23 to every app
```

**After**

```text
/* ogs-blue/ver-android only */
minSdk = 23
```

**Evidence** `6a49531` `8a60754` `00373e1` · Chat · minSdk 23 and landscape

### R143 Platform layout naming

**Scoped · Config**

Preserve ver-mac, ver-ios, ver-android, ver-linux and ver-windows, and matching source roles under src or Android's native app/src tree. Use sample-specific component names instead of generic Root, SelectionApp or a duplicate render module.

**Review check and exceptions**

Validate app manifests refer to the existing source structure. Android's Kotlin paths need not imply additional Kotlin packages.

**Example** (Representative source structure · Windows app)

**Before**

```text
ver-windows/src/SelectionApp.swift
ver-windows/src/selection/view/RenderGLView.swift
```

**After**

```text
ver-windows/src/HSTApp.swift
ver-windows/src/render/view/RenderGLView.swift → components/render/windows/view/RenderGLView.swift
```

**Evidence** `9789b13` `0934da1` `d8df00c` `bf52dd9`

### R144 GTK and Windows native render wrappers

**Scoped · Config**

Keep the native view and toolkit representable separate where required. Prefer a direct rendering widget over extra wrapper layers, while retaining the platform model of embedding and presentation.

**Review check and exceptions**

Do not mechanically add sizeThatFits or a default initializer flag when the counterpart and toolkit do not need it.

**Example** (Representative type layout · native embedding)

**Before**

```text
struct RenderView: View {
    // Construct the native rendering widget alongside app UI
}
```

**After**

```text
RenderView.swift       # toolkit representable/embedding boundary
RenderGLView.swift     # native widget and rendering lifecycle
```

**Evidence** `775fd57` `7bcdb28` `bc20881` · Chat · sizeThatFits and separate view files

### R145 Native bridge logs before setup

**Scoped · Config**

Register desired component logging ahead of setup so first events appear. Keep common console configuration in the app delegate setup area.

**Review check and exceptions**

Detect setup/log call order when both are used, while checking duplicate registrations on repeated lifecycle events.

**Example** (Git diff `1486bc4` · `hello/hello-selection/ver-android/app/src/main/kotlin/org/opengamestudio/HSTActivityDelegate.kt`)

**Before**

```text
    otherSetupConsoleLogging(hst.ctrl, "HST")
    hst.setup()
    render.setup()
    otherSetupConsoleLogging(render.ctrl, "Render")
}
```

**After**

```text
    otherSetupConsoleLogging(hst.ctrl, "HST")
    hst.setup()
    otherSetupConsoleLogging(render.ctrl, "Render")
    render.setup()
}
```

**Evidence** `1486bc4` · Chat · delegate holds blue and setupConsoleLogging

### R146 Shared timing source

**Scoped · Config**

Use the common monotonic time contract on platforms that support it and keep match time platform adaptations in the scoped Render Aux, not in draw or a second app domain.

**Review check and exceptions**

Do not require the same API where it would change semantics or deployment support. Inspect the retained source for the actual clock per port.

**Example** (Representative adaptation · Render match clock)

**Before**

```text
// renderDraw.swift
let elapsed = CACurrentMediaTime() - vm.matchStartTime
```

**After**

```text
// renderDraw.swift
let elapsed = renderMatchTime() - vm.matchStartTime

// HCM RenderAux owns the platform clock API
```

**Evidence** `5c7a8ce` `b64c783` `347554e` · Chat · CATime vs systemUptime

### R147 Preserve local UI text and formatting

**Scoped · Review**

Use the app's established localized strings and formatting helpers for stats, and preserve the exact requested display spelling and typography. Keep stats formatting distinct from animation mechanics.

**Review check and exceptions**

HCM_STATS_PATTERN is Android animation parsing for the numeric portion of current localized stats, not a required naming or regex style.

**Example** (Git diff `8c2ebb0` · `components/hcm/mac/view/HCMView.swift`)

**Before**

```text
VStack(spacing: 16) {
    ZStack(alignment: .topLeading) {
        Text(vm.title)
            .font(.largeTitle)
            .foregroundColor(.white)
            .padding([.top, .leading], 1)
        Text(vm.title)
            .font(.largeTitle)
            .foregroundColor(.black)
    }
```

**After**

```text
VStack(spacing: 16) {
    ZStack(alignment: .topLeading) {
        Text(vm.stats)
            .font(.title)
            .foregroundColor(.black)
        Text(vm.stats)
            .font(.title)
            .foregroundColor(.white)
            .padding([.top, .leading], 1)
    }
    Spacer()
```

**Evidence** `4d683a8` `8c2ebb0` `a0223e1` `2d827fa` `f8005b7`

## Git Budget Windows verification record

On 2026-11-27, the native Windows x64 VM built `hello-platform` with Swift
6.4.2, SwiftCrossUI 0.10.0, and SwiftWinUI 0.2.2. The comparison used the
shared HPView source and preceded the HPVM consolidation. All figures are
uncompressed file sizes in MiB.

| Measure | Debug | Release | Stripped release |
| --- | ---: | ---: | ---: |
| `hello-platform.exe` | 291.59 | 141.67 | 75.50 |
| Redistributable app files | 348.36 | 198.22 | 132.05 |

Stripping saved 66.17 MiB. PE inspection confirmed that the stripped x64
executable retained its DLL imports and icon group 101 and had no DWARF
sections.

### Counting distribution files

Measure the same source and dependencies for each build configuration.
Report executable bytes separately and sum the executable, app resource
bundles, and each required DLL once for the payload. Follow the PE imports
recursively, including delay-load imports, and account for files loaded
through a bootstrapper or bundle. Leave compiler plug-ins, build caches,
object and import libraries, and separately installed prerequisites out of
the app total. State which prerequisites are expected on the destination.

The stripped release measurement counted these files. The Swift DLLs came
from the installed x64 Swift runtime. The total describes a file inventory
that can be staged for distribution.

```text
hello-platform.exe
swift-winui_CWinAppSDK.bundle/Info.plist
swift-winui_CWinAppSDK.bundle/Microsoft.WindowsAppRuntime.Bootstrap.dll
_FoundationICU.dll
BlocksRuntime.dll
dispatch.dll
Foundation.dll
FoundationEssentials.dll
FoundationInternationalization.dll
swift_Concurrency.dll
swift_RegexParser.dll
swift_StringProcessing.dll
swiftCore.dll
swiftCRT.dll
swiftDispatch.dll
swiftObservation.dll
swiftSynchronization.dll
swiftWinSDK.dll
```

The Windows App Runtime and Microsoft Visual C++ Redistributable are
separate prerequisites. The debug payload also required
`swiftSwiftOnoneSupport.dll`. For a later build, derive the inventory from
its actual binaries.

### Build and desktop evidence

The VM was reached through the configured `w11x86` SSH alias and built
through Git Bash using `util/build-windows`. During access setup, the guest
reported `sshd` running on TCP 22, Ethernet on the Public profile, and the
inbound OpenSSH rule enabled for Private. Check service status, port
forwarding, the active firewall profile, and key authentication separately
when the build host is unreachable.

The VM lost SSH during the first release build. VirtualBox logged
`VERR_DISK_FULL` on the Mac host volume backing its virtual disk. After
space was freed and the VM restarted, the release build completed from the
existing checkout and the debug baseline was still available.

Launching WinUI from noninteractive SSH failed during runtime
initialization. In the signed-in Windows desktop session, the stripped
release opened the **Hello platform** window. After the HPVM consolidation,
the macOS and Windows release builds and the iOS Simulator build passed.
The Windows app opened again. The automated check did not confirm that
pressing **Increment** updated the label.

**Evidence** Git Budget `0bde833` `c0a73df` `f4638c5` · Windows x64 build,
PE inspection, and desktop checks, 2026-11-27.
