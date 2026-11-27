final class HPAppDelegate {
    let impl = HPAppImpl()

    func setup() {
        impl.setup()
        otherSetupWindowIcon(101)
    }
}
