package org.opengamestudio

private typealias GC = GitContext

object GitComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.didLaunch, { c: GC -> gitLocateRootDir(vm.androidContext!!) },
            F.filesDir, { c: GC -> gitListFiles(c.filesDir) },
        )
        registerOneliners(gitCtrl(), oneliners)
    }

    fun launch() {
        gitSetupDefaults()
        gitSet(F.didLaunch, true)
    }
}
