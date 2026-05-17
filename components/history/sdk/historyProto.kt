package org.opengamestudio

object HistoryProto {
    val ctrl: KDController

    init {
        ctrl = KDController(HistoryContext())
        setupComponentDebugging(ctrl, "History")
        arrayOf(
            ::historyShouldRead,
            ::historyShouldResetItems,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as HistoryContext) }
        }
    }
}

fun historyCtrl(): KDController {
    return HistoryProto.ctrl
}

fun historyCtrlCtx(): HistoryContext {
    return HistoryProto.ctrl.context as HistoryContext
}

fun historyCtrlCtxField(): String {
    return HistoryProto.ctrl.context.recentField
}

fun historySet(k: String, v: Any) {
    HistoryProto.ctrl.set(k, v)
}
