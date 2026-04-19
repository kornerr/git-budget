package org.opengamestudio

object BudgetProto {
    val ctrl: KDController

    init {
        ctrl = KDController(BudgetContext())
        setupComponentDebugging(ctrl, "Budget")
        arrayOf(
            ::budgetShouldResetMorningBalance,
            ::budgetShouldResetResult,
            ::budgetShouldResetSpent,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as BudgetContext) }
        }
    }
}
