package org.opengamestudio

object BudgProto {
    val ctrl: KDController

    init {
        ctrl = KDController(BudgContext())
        setupComponentDebugging(ctrl, "Budg")
        arrayOf(
            ::budgShouldResetLeft,
            ::budgShouldResetMorningBalance,
            ::budgShouldResetReportedItem,
            ::budgShouldResetResult,
            ::budgShouldResetSpent,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as BudgContext) }
        }
    }
}

fun budgCtrl(): KDController {
    return BudgProto.ctrl
}

fun budgCtrlCtx(): BudgContext {
    return BudgProto.ctrl.context as BudgContext
}

fun budgCtrlCtxField(): String {
    return BudgProto.ctrl.context.recentField
}

fun budgSet(k: String, v: Any) {
    BudgProto.ctrl.set(k, v)
}
