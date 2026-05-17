package org.opengamestudio

fun historyArrayItemsToDict(items: Array<HistoryItem>): Map<String, HistoryItem> {
    var d = mutableMapOf<String, HistoryItem>()
    for (item in items) {
        d[item.dt] = item
    }
    return d
}

fun historyParseItems(lines: Array<String>): Array<HistoryItem> {
    var items = arrayOf<HistoryItem>()
    var item: HistoryItem? = null
    for (ln in lines) {
        if (ln.startsWith(HISTORY_PREFIX_DATE)) {
            val prefixLen = HISTORY_PREFIX_DATE.length
            val value = ln.substring(prefixLen)
            item = HistoryItem()
            item.dt = value
        }
        else if (ln.startsWith(HISTORY_PREFIX_SPENT)) {
            val prefixLen = HISTORY_PREFIX_SPENT.length
            val value = ln.substring(prefixLen)
            item!!.spent = value.toFloat()
        }
        else if (ln.startsWith(HISTORY_PREFIX_LEFT)) {
            val prefixLen = HISTORY_PREFIX_LEFT.length
            val value = ln.substring(prefixLen)
            item!!.left = value.toFloat()
        }

        if (ln.startsWith(HISTORY_PREFIX_LEFT)) {
            items += item!!
        }
    }
    return items
}

fun historyReportedItem(
    items: Map<String, HistoryItem>,
    reportedDt: String,
    spent: String
): HistoryItem {
    var item = HistoryItem()
    item.dt = reportedDt
    item.spent = spent.toFloat()
    item.left = -153f

    return item
}
