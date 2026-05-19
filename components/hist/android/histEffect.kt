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

fun histWriteFile(
    dir: String,
    ditems: Map<String, HistItem>
) {
    val f = File("$dir/$HIST_FILE")
    val items = histDictItemsToArray(ditems)
    //val txt = histItemsToString(items)
    //val lines = f.readLines().toTypedArray()
    /*histSet(F.didWrite, true)*/
}
