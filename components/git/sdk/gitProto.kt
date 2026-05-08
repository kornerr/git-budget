package org.opengamestudio

object GitProto {
    val ctrl: KDController

    init {
        ctrl = KDController(GitContext())
        setupComponentDebugging(ctrl, "Git")
        arrayOf(
            ::gitShouldClone,
            ::gitShouldResetRepoDir,
        ).forEach { f ->
            ctrl.registerFunction { c -> f(c as GitContext) }
        }
    }
}

fun gitCtrl(): KDController {
    return GitProto.ctrl
}

fun gitCtrlCtx(): GitContext {
    return GitProto.ctrl.context as GitContext
}

fun gitCtrlCtxField(): String {
    return GitProto.ctrl.context.recentField
}

fun gitSet(k: String, v: Any) {
    GitProto.ctrl.set(k, v)
}

fun gitSetupDefaults() {
    gitSet(F.repoURL, "https://git.opengamestudio.org/kornerr/private-test")
}
