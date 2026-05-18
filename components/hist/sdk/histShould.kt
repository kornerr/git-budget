package org.opengamestudio

fun histShouldRead(c: HistContext): HistContext {
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

fun histShouldResetItems(c: HistContext): HistContext {
    if (c.recentField == F.loadedItems) {
        c.items = histArrayItemsToDict(c.loadedItems)
        c.recentField = F.items
        return c
    }

    c.recentField = F.none
    return c
}
