package org.opengamestudio

// Launch only once
//
// Purpose: Work around Android's activity restart
//
// Conditions:
// 1. UI has been created the first time
fun budgetShouldLaunch(c: BudgetContext): BudgetContext {
    if (
        c.recentField == F.didSetup &&
        !c.didLaunch
    ) {
        c.didLaunch = true
        c.recentField = F.didLaunch
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
 */
fun budgetShouldResetMorningBalance(c: BudgetContext): BudgetContext {
    /* 1 */ if (c.recentField == "inputMorningBalance") {
        c.morningBalance = c.inputMorningBalance
        c.recentField = "morningBalance"
        return c
    }

    /* 2 */ if (c.recentField == "pastedMorningBalance") {
        c.morningBalance = c.pastedMorningBalance
        c.recentField = "morningBalance"
        return c
    }

    c.recentField = "none"
    return c
}

/* Construct result
 *
 * Conditions:
 * 1. Did launch or specified spent/balance
 */
fun budgetShouldResetResult(c: BudgetContext): BudgetContext {
    if (
        c.recentField == "didLaunch" ||
        c.recentField == "morningBalance" ||
        c.recentField == "spent"
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
        c.recentField = "result"
        return c
    }

    c.recentField = "none"
    return c
}

/* Consolidate spent value
 *
 * Conditions:
 * 1. User did input spent value
 * 2. User did paste spent value
 */
fun budgetShouldResetSpent(c: BudgetContext): BudgetContext {
    /* 1 */ if (c.recentField == "inputSpent") {
        c.spent = c.inputSpent
        c.recentField = "spent"
        return c
    }

    /* 2 */ if (c.recentField == "pastedSpent") {
        c.spent = c.pastedSpent
        c.recentField = "spent"
        return c
    }

    c.recentField = "none"
    return c
}
