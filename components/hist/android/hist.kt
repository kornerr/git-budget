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
        histSet(F.reportedDate, reportedDate(1))
        histSet(F.didLaunch, true)
    }
}
