import gb
import UIKit

class AppDelegate: UIResponder, UIApplicationDelegate {
    let vm = VM()

    func application(
        _: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FunKt.simplyPrint(txt: "qwe")
        return true
    }
}
