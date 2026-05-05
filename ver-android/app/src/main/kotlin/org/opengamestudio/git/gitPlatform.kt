package org.opengamestudio

import java.io.File
import org.eclipse.jgit.api.Git

fun gitClone(
    dir: String,
    url: String
): Boolean {
    try {
        val cr = UsernamePasswordCredentialsProvider(
            "kornerr@gmail.com",
            hash
        )
        val git = Git
            .cloneRepository()
            .setURI(url)
            .setDirectory(File(dir))
            .setCredentialsProvider(cr)
            .call()

        return true
    } catch (e: Exception) {
        println("ИГР gitC url/dir/exception: '$url'/'$dir'/'$e'")
        e.printStackTrace()

        return false
    }
}
