package org.opengamestudio

import java.io.File

fun logRead(dir: String) {
    val f = File("$dir/$LOG_FILE")
    val txt = f.readText()
    logSet(F.logContents, txt)
}
