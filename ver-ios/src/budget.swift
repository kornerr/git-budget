import gb
import UIKit

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
        ctrl.set("reportedDate", budgetReportedDate())
        ctrl.set("reportedWeekday", budgetReportedWeekday())

        setupEffects()
        setupShoulds()
    }

    func setupEffects() {
        var vm = { VM.singleton! }

        var r: BC?
        let effects: [Any] = [
            "didClickPaste", { (c: BC) in budgetPasteSpent(vm()) },
            "pastedSpent", { (c: BC) in vm().inputSpent = c.pastedSpent },
            "result", { (c: BC) in vm().result = c.result },
        ]
        r = registerOneliners(ctrl, effects)
    }

    func setupShoulds() {
      [
        KT.budgetShouldResetResult,
        KT.budgetShouldResetSpent,
      ].forEach { f in
        ctrl.registerFunction { c in f(c as! BC) }
      }
    }
}

//<!-- Effects -->

func budgetPasteSpent(_ vm: VM) {
    let txt = UIPasteboard.general.string ?? "N/A"
    budgetCtrl().set("pastedSpent", txt)
}

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

// Reported week day
// 1 == Monday, ..., 7 == Sunday
func budgetReportedWeekday() -> Int32 {
    let cal = Calendar.current
    let yesterday = cal.date(byAdding: .day, value: -1, to: Date())
    let weekdayApple = cal.component(.weekday, from: yesterday!)
    let weekdayJava = mondayBasedWeekday(weekdayApple)
    return Int32(weekdayJava)
}
