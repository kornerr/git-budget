import AppKit

final class HPAppDelegate:
    NSObject,
    NSApplicationDelegate
{
    let impl = HPAppImpl()

    func applicationDidFinishLaunching(_ notification: Notification) {
        // Bring the app to the foreground
        NSApp.setActivationPolicy(.regular)
        NSApp.activate(ignoringOtherApps: true)

        impl.setup()
    }

    func applicationShouldTerminateAfterLastWindowClosed(_ sender: NSApplication) -> Bool {
        return true
    }
}
