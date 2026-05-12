package org.opengamestudio

// Convert array of log items into dictionary with date as key
fun logArrayItemsToDict(items: Array<LogItem>): Map<String, LogItem> {
    var d = mutableMapOf<String, LogItem>()
    for (item in items) {
        d[item.dt] = item
    }
    return d
}

// Parse raw lines of strings to an array of log items
fun logParseItems(lines: Array<String>): Array<LogItem> {
    var items = arrayOf<LogItem>()
    var item: LogItem? = null
    for (ln in lines) {
        // Date
        if (ln.startsWith(LOG_PREFIX_DATE)) {
            val prefixLen = LOG_PREFIX_DATE.length
            val value = ln.substring(prefixLen)
            item = LogItem()
            item.dt = value
        }
        // Spent
        else if (ln.startsWith(LOG_PREFIX_SPENT)) {
            val prefixLen = LOG_PREFIX_SPENT.length
            val value = ln.substring(prefixLen)
            item!!.spent = value.toFloat()
        }
        // Left
        else if (ln.startsWith(LOG_PREFIX_LEFT)) {
            val prefixLen = LOG_PREFIX_LEFT.length
            val value = ln.substring(prefixLen)
            item!!.left = value.toFloat()
        }

        // Add new item after parsing `Left`
        // NOTE This is not an if-else, it's just if, intentionally
        if (ln.startsWith(LOG_PREFIX_LEFT)) {
            // Parsing has already been done before
            // Now, just add the new item
            items += item!!
        }
    }
    return items
}

// Create reported item
fun logReportedItem(
    items: Map<String, LogItem>,
    reportedDate: String,
    spent: String
): LogItem {
    var item = LogItem()
    item.dt = reportedDate // TODO: YYYY-MM-DD
    item.spent = spent.toFloat()
    item.left = -153f // TODO: Formula

    return item
}