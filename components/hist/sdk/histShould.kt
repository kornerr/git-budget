package org.opengamestudio

// Trigger reading the file
//
// Conditions:
// 1. Cloning succeeded
// 2. Pulling succeeded
fun histShouldRead(c: HistContext): HistContext {
    if (
        /* 1 */ c.recentField == F.didClone ||
        /* 2 */ c.recentField == F.didPull
    ) {
        c.read = true
        c.recentField = F.read
        return c
    }

    c.recentField = F.none
    return c
}

// Reset history of items
//
// Conditions:
// 1. Items have been loaded after reading the file
fun histShouldResetItems(c: HistContext): HistContext {
    /* 1 */ if (c.recentField == F.loadedItems) {
        c.items = histArrayItemsToDict(c.loadedItems)
        c.recentField = F.items
        return c
    }

    c.recentField = F.none
    return c
}

// Reset history of items to be saved
//
// Conditions:
// 1. Newly reported item has been updated
fun histShouldSaveItems(c: HistContext): HistContext {
    /* 1 */ if (c.recentField == F.reportedItem) {
        c.saveItems = histAcceptItem(c.items, c.reportedItem)
        c.recentField = F.saveItems
        return c
    }

    c.recentField = F.none
    return c
}
