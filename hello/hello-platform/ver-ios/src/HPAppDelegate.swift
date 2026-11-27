import UIKit

final class HPAppDelegate:
    NSObject,
    UIApplicationDelegate
{
    let impl = HPAppImpl()

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        impl.setup()
        return true
    }
}
