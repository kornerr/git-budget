import AppKit

final class HPAppDelegate:
    NSObject,
    NSApplicationDelegate
{
    let hp = HPComponent()
    let hpVM = HPVM()

    func applicationDidFinishLaunching(_ notification: Notification) {
        // Bring the app to the foreground
        NSApp.setActivationPolicy(.regular)
        NSApp.activate(ignoringOtherApps: true)

        otherSetupConsoleLogging(hp.ctrl, "HP")
        hp.setup()
    }

    func applicationShouldTerminateAfterLastWindowClosed(_ sender: NSApplication) -> Bool {
        return true
    }
}
