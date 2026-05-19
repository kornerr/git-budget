package org.opengamestudio

/* How much balance left
 *
 * Conditions:
 * 1. User did click commit
 */
fun budgShouldResetLeft(c: BudgContext): BudgContext {
    /* 1 */ if (c.recentField == F.didClickCommit) {
        c.left = budgBalanceLeft(c.morningBalance.toFloat(), c.spent.toFloat())
        c.recentField = F.left
        return c
    }

    c.recentField = F.none
    return c
}

/* Consolidate morning balance value
 *
 * Conditions:
 * 1. User did input morning balance
 * 2. User did paste morning balance
 * 3. Log items have become available
 */
fun budgShouldResetMorningBalance(c: BudgContext): BudgContext {
    /* 1 */ if (c.recentField == F.inputMorningBalance) {
        c.morningBalance = c.inputMorningBalance
        c.recentField = F.morningBalance
        return c
    }

    /* 2 */ if (c.recentField == F.pastedMorningBalance) {
        c.morningBalance = c.pastedMorningBalance
        c.recentField = F.morningBalance
        return c
    }

    /* 3 */ if (c.recentField == F.historyItems) {
        c.morningBalance = budgHistoryBalance(
            c.historyItems,
            c.dateBeforeReported,
            c.weekdayBeforeReported
        )
        c.recentField = F.morningBalance
        return c
    }

    c.recentField = F.none
    return c
}

/* Construct currently reported item
 *
 * Conditions:
 * 1. Remaning balance has been calculated
 */
fun budgShouldResetReportedItem(c: BudgContext): BudgContext {
    /* 1 */ if (c.recentField == F.left) {
        c.reportedItem = HistItem(
            c.reportedDate,
            c.left.toFloat(),
            c.spent.toFloat()
        )
        c.recentField = F.reportedItem
        return c
    }

    c.recentField = F.none
    return c
}

/* Construct result
 *
 * Conditions:
 * 1. Did launch or specified spent/balance
 */
fun budgShouldResetResult(c: BudgContext): BudgContext {
    if (
        c.recentField == F.didLaunch ||
        c.recentField == F.morningBalance ||
        c.recentField == F.spent
    ) {
        val mbalance = budgNumber(budgStringOnlyNumerical(c.morningBalance))
        val spent = budgNumber(budgStringOnlyNumerical(c.spent))
        var lines = arrayOf<String>()
        lines += budgResultDate(c.reportedDate)
        lines += ""
        lines += budgResultSpent(mbalance, c.reportedWeekday, spent)
        lines += budgResultOverrun(mbalance, c.reportedWeekday, spent)
        lines += budgResultLeft(mbalance, c.reportedWeekday, spent)
        c.result = lines.joinToString("\n")
        c.recentField = F.result
        return c
    }

    c.recentField = F.none
    return c
}

/* Consolidate spent value
 *
 * Conditions:
 * 1. User did input spent value
 * 2. User did paste spent value
 */
fun budgShouldResetSpent(c: BudgContext): BudgContext {
    /* 1 */ if (c.recentField == F.inputSpent) {
        c.spent = c.inputSpent
        c.recentField = F.spent
        return c
    }

    /* 2 */ if (c.recentField == F.pastedSpent) {
        c.spent = c.pastedSpent
        c.recentField = F.spent
        return c
    }

    c.recentField = F.none
    return c
}
