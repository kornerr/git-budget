import gb

//!<-- API -->

func budgetCtrl() -> CLDController {
    return BudgetComponent.singleton!.ctrl
}

//!<-- Component -->

private typealias BC = BudgetContext
private typealias KT = BudgetFunKt

class BudgetComponent {
    let ctrl: CLDController
    static private(set) weak var singleton: BudgetComponent?

    init() {
        ctrl = CLDController(context: KT.budgetContext())
        Self.singleton = self

        // TODO

        setupEffects()
        setupShoulds()
    }

    func setupEffects() {
    }

    func setupShoulds() {
    }
}

//<!-- Effects -->

//<!-- Other functions -->
