package org.opengamestudio

private typealias HC = HistContext

object HistComponent {
    init {
        val oneliners = arrayOf(
            F.fileLines, { c: HC -> histParse(c.fileLines) },
            F.read, { c: HC -> histReadFile(c.repoDir) },
        )
        registerOneliners(histCtrl(), oneliners)
    }

    fun launch() {
        histSet(F.didLaunch, true)
    }
}
