import gb
import UIKit

//!<-- Component -->

private typealias BC = BudgetContext

class BudgetComponent {
    init() {
        var vm = { VM.singleton! }

        var r: BC?
        let effects: [Any] = [
            F.didClickCopy, { (c: BC) in budgetCopyResult(c.result) },
            F.didClickPasteMorningBalance, { (c: BC) in budgetPasteMorningBalance(vm()) },
            F.didClickPasteSpent, { (c: BC) in budgetPasteSpent(vm()) },
            F.morningBalance, { (c: BC) in vm().morningBalance = c.morningBalance },
            F.spent, { (c: BC) in vm().spent = c.spent },
            F.result, { (c: BC) in vm().result = c.result },
        ]
        r = registerOneliners(ctrl, effects)

        // Defaults
        budgetSet(F.reportedDate, budgetReportedDate())
        budgetSet(F.reportedWeekday, budgetReportedWeekday())
    }

    func setup() {
        budgetSet(F.didSetup, true)
    }
}

//<!-- Effects -->

func budgetCopyResult(_ result: String) {
    UIPasteboard.general.string = result
}

func budgetPasteMorningBalance(_ vm: VM) {
    let txt = UIPasteboard.general.string ?? "N/A"
    budgetCtrl().set("pastedMorningBalance", txt)
}

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
