import SwiftUI

@main
struct AppView: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            BudgetView(appDelegate.vm)
        }
    }
}
