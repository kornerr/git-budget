package org.opengamestudio

private typealias BC = BudgContext

object BudgComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.didClickCopy, { c: BC -> budgCopyResult(vm.androidContext!!, c.result) },
            F.didClickPasteMorningBalance, { c: BC -> budgPasteMorningBalance(vm.androidContext!!) },
            F.didClickPasteSpent, { c: BC -> budgPasteSpent(vm.androidContext!!) },
            F.isSaveEnabled, { c: BC -> vm.isSaveEnabled.value = c.isSaveEnabled },
            F.morningBalance, { c: BC -> vm.morningBalance.value = c.morningBalance },
            F.saveTitle, { c: BC -> vm.saveTitle.value = c.saveTitle },
            F.spent, { c: BC -> vm.spent.value = c.spent },
            F.result, { c: BC -> vm.result.value = c.result },
        )
        registerOneliners(budgCtrl(), oneliners)
    }

    fun launch() {
        // Defaults
        budgSet(F.dateBeforeReported, reportedDate(2))
        budgSet(F.reportedDate, reportedDate(1))
        budgSet(F.reportedWeekday, reportedWeekday(1))
        budgSet(F.weekdayBeforeReported, reportedWeekday(2))

        budgSet(F.didLaunch, true)
    }
}
