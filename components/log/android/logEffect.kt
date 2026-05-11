package org.opengamestudio

import java.io.File

fun logParse(lines: Array<String>) {
    val items = logParseItems(lines)
    logSet(F.loadedItems, items)
}

fun logReadFile(dir: String) {
    val f = File("$dir/$LOG_FILE")
    val lines = f.readLines().toTypedArray()
    logSet(F.fileLines, lines)
}
