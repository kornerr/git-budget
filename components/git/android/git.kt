package org.opengamestudio

private typealias GC = GitContext

object GitComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.didLaunch, { c: GC -> gitLocateRootDir(vm.androidContext!!) },
            F.repoDir, { c: GC -> gitCheckRepoDirAvailability(c.repoDir) },
        )
        registerOneliners(gitCtrl(), oneliners)
    }

    fun launch() {
        gitSetupDefaults()
        gitSet(F.didLaunch, true)
    }
}
