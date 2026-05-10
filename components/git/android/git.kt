package org.opengamestudio

private typealias GC = GitContext

object GitComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.clone, { c: GC -> gitClone(c.repoDir, c.repoURL) },
            //F.commit, { c: GC -> gitCommit(c.repoDir) },
            F.didLaunch, { c: GC -> gitLocateRootDir(vm.androidContext!!) },
            F.pull, { c: GC -> gitPull(c.repoDir) },
            //F.push, { c: GC -> gitPush(c.repoDir) },
            F.repoDir, { c: GC -> gitCheckRepoDirAvailability(c.repoDir) },
        )
        registerOneliners(gitCtrl(), oneliners)
    }

    fun launch() {
        gitSetupDefaults()
        gitSet(F.didLaunch, true)
    }
}
