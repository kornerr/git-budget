package org.opengamestudio

import java.io.File

fun histParse(lines: Array<String>) {
    val items = histParseItems(lines)
    histSet(F.loadedItems, items)
}

fun histReadFile(dir: String) {
    val f = File("$dir/$HIST_FILE")
    val lines = f.readLines().toTypedArray()
    histSet(F.fileLines, lines)
}
