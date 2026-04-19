package org.opengamestudio

object BudgetProto {
    val ctrl: KDController

    init {
        ctrl = KDController(BudgetContext())
        setupComponentDebugging(ctrl, "Budget")
        arrayOf(
            ::budgetShouldLaunch,
            ::budgetShouldResetMorningBalance,
            ::budgetShouldResetResult,
            ::budgetShouldResetSpent,
            ::budgetShouldResetSpentCombined,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as BudgetContext) }
        }
    }
}
