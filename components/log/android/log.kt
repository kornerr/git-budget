package org.opengamestudio

private typealias LC = LogContext

object LogComponent {
    init {
        val oneliners = arrayOf(
            F.didRead, { c: LC -> logRead() },
        )
        registerOneliners(logCtrl(), oneliners)
    }

    fun launch() {
        logSet(F.didLaunch, true)
    }
}
