package org.opengamestudio

/* Read log
 *
 * Conditions:
 * 1. Did launch
 */
fun logShouldRead(c: LogContext): LogContext {
    /* 1 */ if (c.recentField == F.didLaunch) {
        c.read = true
        c.recentField = F.read
        return c
    }

    c.recentField = F.none
    return c
}
