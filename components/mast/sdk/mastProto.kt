package org.opengamestudio

object MastProto {
    val ctrl: KDController

    init {
        ctrl = KDController(MastContext())
        setupComponentDebugging(ctrl, "Mast")
        arrayOf(
            ::mastShouldLaunch,
            ::mastShouldResetBudgetTabSelection,
            ::mastShouldResetLogs,
            ::mastShouldResetSettingsTabSelection,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as MastContext) }
        }
    }
}

fun mastCtrl(): KDController {
    return MastProto.ctrl
}

fun mastCtrlCtx(): MastContext {
    return MastProto.ctrl.context as MastContext
}

fun mastCtrlCtxField(): String {
    return MastProto.ctrl.context.recentField
}

fun mastSet(k: String, v: Any) {
    MastProto.ctrl.set(k, v)
}
