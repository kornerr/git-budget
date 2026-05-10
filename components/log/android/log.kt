package org.opengamestudio

private typealias LC = LogContext

object LogComponent {
    init {
        val oneliners = arrayOf(
            F.read, { c: LC -> logRead(c.repoDir) },
        )
        registerOneliners(logCtrl(), oneliners)
    }

    fun launch() {
        logSet(F.didLaunch, true)
    }
}
