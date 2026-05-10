package org.opengamestudio

import java.io.File

fun logRead(dir: String) {
    val f = File("$dir/$LOG_FILE")
    val txt = f.readText(Charsets.UTF_8)
    println("ИГР logR len/txt: '${txt.length}'/'$txt'")
    logSet(F.logContents, txt)
}
