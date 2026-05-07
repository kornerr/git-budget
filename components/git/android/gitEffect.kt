package org.opengamestudio

import android.content.Context

/*
import java.io.File
import kotlinx.coroutines.*
*/

fun gitListFiles(dir: String) {
    println("ИГР gitLF dir: '$dir'")
}

fun gitLocateRootDir(ctx: Context) {
    val dir = ctx.getExternalFilesDir(null)?.absolutePath ?: "N/A"
    gitSet(F.rootDir, dir)
}

        /*
        //val url = "https://github.com/OGStudio/kotlin-dialect"
        val url = "https://git.opengamestudio.org/kornerr/private-test"
        val dir = ctx.getExternalFilesDir(null)?.absolutePath + "/cloned-repo"
        var root = File(dir)
        //if (!root.exists()) {
            GlobalScope.launch(Dispatchers.IO/* + exceptionHandler*/) {
                // 1. Clone
                println("ИГР GitC.setup-01 before clone")
                val resClone = gitClone(dir, url)
                println("ИГР GitC.setup-02 resC: '$resClone'")
            
                // 2. List files
                val files = root.listFiles()
                println("ИГР GitC.setup-02 files:")
                for (item in files) {
                    println("ИГР > '$item'")
                }

                // 3. Print `abc`
                val contents = File(dir + "/abc").readText()
                println("ИГР GitC.setup-03 contents: '$contents'")
            }

        } else {
            println("ИГР GitC.setup-0X no need to clone")
        }
        */
