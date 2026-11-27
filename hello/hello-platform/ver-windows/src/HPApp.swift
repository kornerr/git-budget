import SwiftCrossUI
import WinUIBackend

@main
struct HPApp: App {
    let delegate = HPAppDelegate()

    var body: some Scene {
        WindowGroup("Hello platform") {
            HPView(delegate.impl.hpVM)
                .onAppear {
                    delegate.setup()
                }
        }
    }
}
