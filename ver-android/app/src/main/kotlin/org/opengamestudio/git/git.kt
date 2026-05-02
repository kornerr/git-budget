package org.opengamestudio

import android.content.Context
import java.io.File
import org.eclipse.jgit.api.Git

//private typealias GC = GitContext

object GitComponent {
    init {
        println("ИГР GitC.init-01")

    }

    fun setup(androidContext: Context) {
        val location = androidContext.getExternalFilesDir(null)?.absolutePath + "/cloned-repo"
        try {
            val git = Git
                .cloneRepository()
                .setURI("https://github.com/OGStudio/kotlin-dialect")
                .setDirectory(File(location))
                .call()
            println("ИГР GitC.init-02")
        } catch (e: Exception) {
            println("ИГР GitC.init-03 exception: '$e'")
            e.printStackTrace()
        }
    }
}
