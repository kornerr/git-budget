import gb

private typealias BC = BudgetContext

class BudgetComponent {
    init() {
        var vm = { VM.singleton! }

        let effects: [Any] = [
            F.didClickCopy, { (c: BC) in budgetCopyResult(c.result) },
            F.didClickPasteMorningBalance, { (c: BC) in budgetPasteMorningBalance() },
            F.didClickPasteSpent, { (c: BC) in budgetPasteSpent() },
            F.morningBalance, { (c: BC) in vm().morningBalance = c.morningBalance },
            F.spent, { (c: BC) in vm().spent = c.spent },
            F.result, { (c: BC) in vm().result = c.result },
        ]
        var r: BC? = registerOneliners(budgetCtrl(), effects)
    }

    func setup() {
        // Defaults
        budgetSet(F.reportedDate, budgetReportedDate())
        budgetSet(F.reportedWeekday, budgetReportedWeekday())
        budgetSet(F.didSetup, true)
    }
}
