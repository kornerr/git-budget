package org.opengamestudio

fun logShouldReadOnLaunch(c: LogContext): LogContext {
    if (c.recentField == F.didLaunch) {
        c.didRead = true
        c.recentField = F.didRead
        return c
    }

    c.recentField = F.none
    return c
}
