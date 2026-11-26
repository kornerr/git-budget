package org.opengamestudio

private typealias HC = HistContext

object HistComponent {
    init {
        val oneliners = arrayOf(
            F.fileLines, { c: HC -> histParse(c.fileLines) },
            F.read, { c: HC -> histReadFile(c.repoDir) },
            F.saveItems, { c: HC -> histWriteFile(c.repoDir, c.saveItems) },
        )
        registerOneliners(histCtrl(), oneliners)
    }

    fun launch() {
        histSet(F.didLaunch, true)
    }
}
