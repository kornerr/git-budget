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
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as BudgetContext) }
        }
    }
}

fun budgetCtrl(): KDController {
    return BudgetProto.ctrl
}

fun budgetCtrlCtx(): BudgetContext {
    return BudgetProto.ctrl.context as BudgetContext
}

fun budgetCtrlCtxField(): String {
    return BudgetProto.ctrl.context.recentField
}

