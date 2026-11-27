import AppKit
import SwiftUI

@main
struct HPApp: App {
    @NSApplicationDelegateAdaptor(HPAppDelegate.self) private var appDelegate

    var body: some Scene {
        WindowGroup {
            HPView(appDelegate.impl.hpVM)
        }
            .defaultSize(width: 640, height: 480)
    }
}
