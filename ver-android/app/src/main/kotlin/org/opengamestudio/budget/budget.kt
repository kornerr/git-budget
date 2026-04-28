package org.opengamestudio

private typealias BC = BudgetContext

object BudgetComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.didClickCopy, { c: BC -> budgetCopyResult(vm.androidContext!!, c.result) },
            F.didClickPasteMorningBalance, { c: BC -> budgetPasteMorningBalance(vm.androidContext!!) },
            F.didClickPasteSpent, { c: BC -> budgetPasteSpent(vm.androidContext!!) },
            F.morningBalance, { c: BC -> vm.morningBalance.value = c.morningBalance },
            F.spent, { c: BC -> vm.spent.value = c.spent },
            F.result, { c: BC -> vm.result.value = c.result },
        )
        registerOneliners(budgetCtrl(), oneliners)
    }

    fun setup() {
        // Defaults
        budgetSet(F.reportedDate, budgetReportedDate())
        budgetSet(F.reportedWeekday, budgetReportedWeekday())
        budgetSet(F.didSetup, true)
    }
}
