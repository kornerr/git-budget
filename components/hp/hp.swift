class HPComponent {
    let ctrl = DialectController(HPContext())
    static private(set) weak var singleton: HPComponent?

    init() {
        Self.singleton = self
        hpRegisterEffects(ctrl)
        hpRegisterShoulds(ctrl)
    }

    func setup() {
        hpSet(F.didSetup, true)
    }
}
