package org.opengamestudio

/* Read log
 *
 * Conditions:
 * 1. Did clone / pull successfully
 */
fun logShouldRead(c: LogContext): LogContext {
    /* 1 */ if (
        c.recentField == F.didClone ||
        c.recentField == F.didPull
    ) {
        c.read = true
        c.recentField = F.read
        return c
    }

    c.recentField = F.none
    return c
}
