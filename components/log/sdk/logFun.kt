package org.opengamestudio

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
