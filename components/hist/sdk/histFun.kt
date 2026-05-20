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

fun histItemsToString(items: Array<HistItem>): String {
    var o = ""
    for (item in items) {
        o += "\n$HIST_PREFIX_DATE${item.dt}"
        o += "\n$HIST_PREFIX_SPENT${item.spent}"
        o += "\n$HIST_PREFIX_LEFT${item.left}"
        o += "\n"
    }
    /**/println("ИГР histITS o: '$o'")
    return o
}

fun histParseItems(lines: Array<String>): Array<HistItem> {
    var items = arrayOf<HistItem>()
    var item: HistItem? = null
    for (ln in lines) {
        /**/println("ИГР histPI-01 ln: '$ln'")
        if (ln.startsWith(HIST_PREFIX_DATE)) {
            val prefixLen = HIST_PREFIX_DATE.length
            val value = ln.substring(prefixLen)
            /**/println("ИГР histPI-02 prefixL/value: '$prefixLen'/'$value'")
            item = HistItem()
            item.dt = value
        }
        else if (ln.startsWith(HIST_PREFIX_SPENT)) {
            val prefixLen = HIST_PREFIX_SPENT.length
            val value = ln.substring(prefixLen)
            /**/println("ИГР histPI-03 prefixL/value: '$prefixLen'/'$value'")
            item!!.spent = value.toFloat()
        }
        else if (ln.startsWith(HIST_PREFIX_LEFT)) {
            val prefixLen = HIST_PREFIX_LEFT.length
            val value = ln.substring(prefixLen)
            /**/println("ИГР histPI-04 prefixL/value: '$prefixLen'/'$value'")
            item!!.left = value.toFloat()
        }

        if (ln.startsWith(HIST_PREFIX_LEFT)) {
            items += item!!
        }
    }
    return items
}
