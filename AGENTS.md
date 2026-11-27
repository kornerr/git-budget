# Git Budget development guide

Git Budget is being rebuilt using the architecture of the sibling `../mj`
repository. The code in `_v2-kd/` and the existing `README.md` are historical
references for the previous implementation.

## Architecture

Use `../mj/hello/hello-platform/` as the baseline for native app structure, Dialect v4, and platform build and launch entry points. Read [mj's development guide](../mj/AGENTS.md), the [component ownership guide](../mj/docs/components-over-platforms.md), and the [hello-platform guide](../mj/docs/hello-platform.md) before scaffolding or porting a target. For cross-platform changes, also read the [mirroring guide](../mj/docs/mirror.md).

- Keep the sample's `dialect.yml`, `util/`, and platform targets together under `hello/hello-platform/`. The shared generator launcher lives at `util/run-generator` at the repository root.
- Keep reusable handwritten source under `components/<component>/`. Put distinct implementations under their platform directories. Use tracked relative symlinks to include shared files in platform targets such as `ver-mac/` and `ver-android/`. Edit the canonical files and verify that the links resolve.
- Keep the app's fields, transitions, and effects in its Dialect v4 `dialect.yml`. Each should branch changes one field. Regenerate the configured Swift and Kotlin outputs after schema changes. Compatible Swift targets link directly to the canonical macOS output.
- Keep native entry points and delegates focused on composition and lifecycle. Views dispatch input through Dialect, and generated effects publish presentation state to the component's view model. Component code accesses its own VM instances and passes values between components through Dialect.
- When mirroring a platform, inspect the closest working counterpart. Preserve names, argument meanings, call order, and event behavior. Keep toolkit-specific behavior in the platform implementation.

## Code review

Use [the code review guide](docs/review.md) as the review rulebook. Apply rules
within their stated scope, honor documented exceptions, and cite rule IDs in
findings.
