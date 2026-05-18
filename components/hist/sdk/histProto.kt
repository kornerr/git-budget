package org.opengamestudio

object HistProto {
    val ctrl: KDController

    init {
        ctrl = KDController(HistContext())
        setupComponentDebugging(ctrl, "Hist")
        arrayOf(
            ::histShouldRead,
            ::histShouldResetItems,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as HistContext) }
        }
    }
}

fun histCtrl(): KDController {
    return HistProto.ctrl
}

fun histCtrlCtx(): HistContext {
    return HistProto.ctrl.context as HistContext
}

fun histCtrlCtxField(): String {
    return HistProto.ctrl.context.recentField
}

fun histSet(k: String, v: Any) {
    HistProto.ctrl.set(k, v)
}
