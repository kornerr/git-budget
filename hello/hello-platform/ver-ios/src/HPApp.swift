import SwiftUI

@main
struct HPApp: App {
    @UIApplicationDelegateAdaptor(HPAppDelegate.self) private var appDelegate

    var body: some Scene {
        WindowGroup {
            HPView(appDelegate.impl.hpVM)
        }
    }
}
