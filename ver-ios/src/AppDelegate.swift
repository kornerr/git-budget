import UIKit

class AppDelegate: UIResponder, UIApplicationDelegate {
    let budgetCmp = BudgetComponent()
    let vm = VM()

    func application(
        _: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        budgetCtrl().set("didLaunch", true)

        return true
    }
}
