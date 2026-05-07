package org.opengamestudio

/* Consolidate morning balance value
 *
 * Conditions:
 * 1. User did input morning balance
 * 2. User did paste morning balance
 */
fun budgetShouldResetMorningBalance(c: BudgetContext): BudgetContext {
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

    c.recentField = F.none
    return c
}

/* Construct result
 *
 * Conditions:
 * 1. Did launch or specified spent/balance
 */
fun budgetShouldResetResult(c: BudgetContext): BudgetContext {
    if (
        c.recentField == F.didLaunch ||
        c.recentField == F.morningBalance ||
        c.recentField == F.spent
    ) {
        val mbalance = budgetNumber(budgetStringOnlyNumerical(c.morningBalance))
        val spent = budgetNumber(budgetStringOnlyNumerical(c.spent))
        var lines = arrayOf<String>()
        lines += budgetResultDate(c.reportedDate)
        lines += ""
        lines += budgetResultSpent(mbalance, c.reportedWeekday, spent)
        lines += budgetResultOverrun(mbalance, c.reportedWeekday, spent)
        lines += budgetResultLeft(mbalance, c.reportedWeekday, spent)
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
fun budgetShouldResetSpent(c: BudgetContext): BudgetContext {
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
