final class HPAppImpl {
    let hp = HPComponent()
    let hpVM = HPVM()

    func setup() {
        otherSetupConsoleLogging(hp.ctrl, "HP")
        hp.setup()
    }
}
