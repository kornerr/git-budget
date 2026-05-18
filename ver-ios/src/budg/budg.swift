import gb

private typealias BC = BudgContext

class BudgComponent {
    init() {
        var vm = { VM.singleton! }

        let effects: [Any] = [
            F.didClickCopy, { (c: BC) in budgCopyResult(c.result) },
            F.didClickPasteMorningBalance, { (c: BC) in budgPasteMorningBalance() },
            F.didClickPasteSpent, { (c: BC) in budgPasteSpent() },
            F.morningBalance, { (c: BC) in vm().morningBalance = c.morningBalance },
            F.spent, { (c: BC) in vm().spent = c.spent },
            F.result, { (c: BC) in vm().result = c.result },
        ]
        var r: BC? = registerOneliners(budgCtrl(), effects)
    }

    func setup() {
        // Defaults
        budgSet(F.reportedDate, budgReportedDate())
        budgSet(F.reportedWeekday, budgReportedWeekday())
        budgSet(F.didSetup, true)
    }
}
