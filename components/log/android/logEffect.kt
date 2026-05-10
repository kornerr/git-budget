package org.opengamestudio

import java.io.File

fun logRead(dir: String) {
    val f = File("$dir/$LOG_FILE")
    val lines = f.readLines().toTypedArray()
    println("ИГР logR-01 begin")
    for (ln in lines) {
        println("ИГР logR-02 ln: '$ln'")
    }
    println("ИГР logR-03 end")
    logSet(F.logLines, lines)
}
