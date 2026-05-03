package org.opengamestudio

import java.io.File
import org.eclipse.jgit.api.Git

fun gitClone(
    dir: String,
    url: String
): Boolean {
    try {
        val git = Git
            .cloneRepository()
            .setURI(url)
            .setDirectory(File(dir))
            .call()

        return true
    } catch (e: Exception) {
        println("ИГР gitC url/dir/exception: '$url'/'$dir'/'$e'")
        e.printStackTrace()

        return false
    }
}
