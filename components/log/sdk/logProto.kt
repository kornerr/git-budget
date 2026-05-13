package org.opengamestudio

object LogProto {
    val ctrl: KDController

    init {
        ctrl = KDController(LogContext())
        setupComponentDebugging(ctrl, "Log")
        arrayOf(
            ::logShouldRead,
            ::logShouldResetItems,
            //::logShouldResetReportedItem,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as LogContext) }
        }
    }
}

fun logCtrl(): KDController {
    return LogProto.ctrl
}

fun logCtrlCtx(): LogContext {
    return LogProto.ctrl.context as LogContext
}

fun logCtrlCtxField(): String {
    return LogProto.ctrl.context.recentField
}

fun logSet(k: String, v: Any) {
    LogProto.ctrl.set(k, v)
}
