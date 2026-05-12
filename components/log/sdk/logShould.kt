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

/* Reset items
 *
 * Conditions:
 * 1. Loaded items
 */
fun logShouldResetItems(c: LogContext): LogContext {
    /* 1 */ if (c.recentField == F.loadedItems) {
        c.items = logArrayItemsToDict(c.loadedItems)
        c.recentField = F.items
        return c
    }

    c.recentField = F.none
    return c
}

/* Reset reported (yesterday's) item
 *
 * Conditions:
 * 1. Spent has been changed
 */
fun logShouldResetReportedItem(c: LogContext): LogContext {
    /* 1 */ if (c.recentField == F.spent) {
        c.reportedItem = logReportedItem(c.items, c.reportedDate, c.spent)
        c.recentField = F.reportedItem
        return c
    }

    c.recentField = F.none
    return c
}
