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

        // Default values
        ctrl.set(fieldName: "reportedDate", value: budgetReportedDate())
        //ctrl.set(fieldName: "reportedWeekday", value: budgetReportedWeekday())

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

// Date of the report (yesterday)
func budgetReportedDate() -> String {
    let cal = Calendar.current
    let yesterday = cal.date(byAdding: .day, value: -1, to: Date())
    let parts = cal.dateComponents([.day, .month], from: yesterday!)
    let sday = String(format: "%02d", parts.day!)
    let smon = String(format: "%02d", parts.month!)
    return "\(sday).\(smon)"
}
