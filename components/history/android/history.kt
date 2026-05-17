package org.opengamestudio

private typealias HC = HistoryContext

object HistoryComponent {
    init {
        val oneliners = arrayOf(
            F.fileLines, { c: HC -> historyParse(c.fileLines) },
            F.read, { c: HC -> historyReadFile(c.repoDir) },
        )
        registerOneliners(historyCtrl(), oneliners)
    }

    fun launch() {
        historySet(F.reportedDate, reportedDate(1))
        historySet(F.didLaunch, true)
    }
}
