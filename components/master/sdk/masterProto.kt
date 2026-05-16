package org.opengamestudio

object MasterProto {
    val ctrl: KDController

    init {
        ctrl = KDController(MasterContext())
        setupComponentDebugging(ctrl, "Master")
        arrayOf(
            ::masterShouldLaunch,
            ::masterShouldLogChange,
            ::masterShouldResetBudgetTabSelection,
            ::masterShouldResetSettingsTabSelection,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as MasterContext) }
        }
    }
}

fun masterCtrl(): KDController {
    return MasterProto.ctrl
}

fun masterCtrlCtx(): MasterContext {
    return MasterProto.ctrl.context as MasterContext
}

fun masterCtrlCtxField(): String {
    return MasterProto.ctrl.context.recentField
}

fun masterSet(k: String, v: Any) {
    MasterProto.ctrl.set(k, v)
}
