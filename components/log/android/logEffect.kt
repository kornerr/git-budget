package org.opengamestudio

import java.io.File

fun logRead(dir: String) {
    val f = File("$dir/$LOG_FILE")
    val txt = f.readString()
    println("ИГР logR txt: '$txt'")
    logSet(F.logContents, txt)
}
