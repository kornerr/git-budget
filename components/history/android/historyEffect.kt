package org.opengamestudio

import java.io.File

fun historyParse(lines: Array<String>) {
    val items = historyParseItems(lines)
    historySet(F.loadedItems, items)
}

fun historyReadFile(dir: String) {
    val f = File("$dir/$HISTORY_FILE")
    val lines = f.readLines().toTypedArray()
    historySet(F.fileLines, lines)
}
