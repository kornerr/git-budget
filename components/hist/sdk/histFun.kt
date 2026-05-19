package org.opengamestudio

fun histAcceptItem(
    was: Map<String, HistItem>,
    item: HistItem
): Map<String, HistItem> {
    var d = was.toMutableMap()
    d[item.dt] = item
    return d
}

fun histArrayItemsToDict(items: Array<HistItem>): Map<String, HistItem> {
    var d = mutableMapOf<String, HistItem>()
    for (item in items) {
        d[item.dt] = item
    }
    return d
}

fun histDictItemsToArray(ditems: Map<String, HistItem>): Array<HistItem> {
    var items = arrayOf<HistItem>()
    val keys = ditems.keys.sorted()
    for (dt in keys) {
        items += ditems[dt]!!
    }
    return items
}

fun histParseItems(lines: Array<String>): Array<HistItem> {
    var items = arrayOf<HistItem>()
    var item: HistItem? = null
    for (ln in lines) {
        if (ln.startsWith(HIST_PREFIX_DATE)) {
            val prefixLen = HIST_PREFIX_DATE.length
            val value = ln.substring(prefixLen)
            item = HistItem()
            item.dt = value
        }
        else if (ln.startsWith(HIST_PREFIX_SPENT)) {
            val prefixLen = HIST_PREFIX_SPENT.length
            val value = ln.substring(prefixLen)
            item!!.spent = value.toFloat()
        }
        else if (ln.startsWith(HIST_PREFIX_LEFT)) {
            val prefixLen = HIST_PREFIX_LEFT.length
            val value = ln.substring(prefixLen)
            item!!.left = value.toFloat()
        }

        if (ln.startsWith(HIST_PREFIX_LEFT)) {
            items += item!!
        }
    }
    return items
}

fun histReportedItem(
    items: Map<String, HistItem>,
    reportedDt: String,
    spent: String
): HistItem {
    var item = HistItem()
    item.dt = reportedDt
    item.spent = spent.toFloat()
    item.left = -153f

    return item
}
