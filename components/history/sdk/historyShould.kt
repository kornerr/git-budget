package org.opengamestudio

fun historyShouldRead(c: HistoryContext): HistoryContext {
    if (
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

fun historyShouldResetItems(c: HistoryContext): HistoryContext {
    if (c.recentField == F.loadedItems) {
        c.items = historyArrayItemsToDict(c.loadedItems)
        c.recentField = F.items
        return c
    }

    c.recentField = F.none
    return c
}
