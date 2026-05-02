package org.opengamestudio

import java.io.File
import org.eclipse.jgit.api.Git

//private typealias GC = GitContext

object GitComponent {
    init {
        println("ИГР GitC.init-01")

        try {
            val git = Git
                .cloneRepository()
                .setURI("https://github.com/OGStudio/kotlin-dialect")
                .setDirectory(File("cloned-repo"))
                .call()
            println("ИГР GitC.init-02")
        } catch (e: Exception) {
            println("ИГР GitC.init-03 exception: '$e'")
            e.printStackTrace()
        }
    }

    fun setup() {
    }
}
