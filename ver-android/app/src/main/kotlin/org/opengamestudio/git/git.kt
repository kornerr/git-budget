package org.opengamestudio

import android.content.Context
import java.io.File
import kotlinx.coroutines.*

//private typealias GC = GitContext

object GitComponent {
    init {
        println("ИГР GitC.init-01")
    }

    fun setup(ctx: Context) {
        val url = "https://github.com/OGStudio/kotlin-dialect"
        val dir = ctx.getExternalFilesDir(null)?.absolutePath + "/cloned-repo"
        var root = File(dir)
        if (!root.exists()) {
            GlobalScope.launch(Dispatchers.IO/* + exceptionHandler*/) {
                val resClone = gitClone(dir, url)
                println("ИГР GitC.setup-01 resC: '$resClone'")
            }
        } else {
            println("ИГР GitC.setup-0X no need to clone")
        }

        val files = root.listFiles()
        println("ИГР GitC.setup-02 files:")
        for (item in files) {
            println("ИГР > '$item'")
        }
    }
}
