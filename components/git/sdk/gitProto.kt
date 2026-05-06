package org.opengamestudio

object GitProto {
    val ctrl: KDController

    init {
        ctrl = KDController(GitContext())
        setupComponentDebugging(ctrl, "Git")
        arrayOf(
            ::gitShouldResetFilesDir,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as GitContext) }
        }
    }
}

fun gitCtrl(): KDController {
    return GitProto.ctrl
}

fun gitCtrlCtx(): GittContext {
    return GitProto.ctrl.context as GitContext
}

fun gitCtrlCtxField(): String {
    return GitProto.ctrl.context.recentField
}

fun gitSet(k: String, v: Any) {
    GitProto.ctrl.set(k, v)
}
