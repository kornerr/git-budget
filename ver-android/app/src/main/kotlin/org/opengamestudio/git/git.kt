package org.opengamestudio

import android.content.Context
import java.io.File
import org.eclipse.jgit.api.Git

//private typealias GC = GitContext

object GitComponent {
    init {
        println("ИГР GitC.init-01")
    }

    fun setup(ctx: Context) {
        val url = "https://github.com/OGStudio/kotlin-dialect"
        val dir = ctx.getExternalFilesDir(null)?.absolutePath + "/cloned-repo"
        if (!File(dir).exists()) {
            val resClone = gitClone(dir, url)
            println("ИГР GitC.init-02 resC: '$resClone'")
        } else {
            println("ИГР GitC.init-03 no need to clone")
        }
    }
}
