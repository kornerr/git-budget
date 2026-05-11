package org.opengamestudio

import java.io.File

fun logParse(lines: Array<String>) {
    val items = logParseItems(lines)
    logSet(F.items, items)
}

fun logReadFile(dir: String) {
    val f = File("$dir/$LOG_FILE")
    val lines = f.readLines().toTypedArray()
    println("ИГР logR-01 begin")
    for (ln in lines) {
        println("ИГР logR-02 ln: '$ln'")
    }
    println("ИГР logR-03 end")
    logSet(F.fileLines, lines)
}
