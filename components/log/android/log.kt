package org.opengamestudio

private typealias LC = LogContext

object LogComponent {
    init {
        val oneliners = arrayOf(
            F.fileLines, { c: LC -> logParse(c.fileLines) },
            F.read, { c: LC -> logReadFile(c.repoDir) },
        )
        registerOneliners(logCtrl(), oneliners)
    }

    fun launch() {
        logSet(F.reportedDateYear, logReportedDateYear())
        logSet(F.didLaunch, true)
    }
}
