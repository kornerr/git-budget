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

        // Debug
        ctrl.registerCallback { (c) in 
          let value = c.field(name: c.recentField)
          print("ИГР BudgetC.init ctrl k/v: '\(c.recentField)'/'\(value)'")
        }

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
