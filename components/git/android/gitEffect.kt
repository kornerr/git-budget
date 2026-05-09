package org.opengamestudio

import android.content.Context
import java.io.File
import kotlinx.coroutines.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.*

fun gitCheckRepoDirAvailability(dir: String) {
    val exists = File(dir).exists()
    gitSet(F.repoDirExists, exists)
}

fun gitClone(
    dir: String,
    url: String
) {
    val cr = UsernamePasswordCredentialsProvider(
        "kornerr@gmail.com",
        hash
    )
    val client = Git
        .cloneRepository()
        .setURI(url)
        .setDirectory(File(dir))
        .setCredentialsProvider(cr)

    GlobalScope.launch(Dispatchers.IO/* + exceptionHandler*/) {
        try {
            client.call()
            GlobalScope.launch(Dispatchers.Main) {
                gitSet(F.didClone, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalScope.launch(Dispatchers.Main) {
                gitSet(F.cloneError, "$e")
            }
        }
    }
}

fun gitCommit(dir: String) {



    val datetime = "${java.time.LocalDateTime.now()}"

    // TMP: Move outside
    File(dir + "/gb.log").appendText("\n$datetime")


    val client = Git
        .open(File(dir))
        .commit()
        .setAll(true)
        .setMessage(datetime)

    GlobalScope.launch(Dispatchers.IO) {
        try {
            client.call()
            GlobalScope.launch(Dispatchers.Main) {
                gitSet(F.didCommit, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalScope.launch(Dispatchers.Main) {
                gitSet(F.commitError, "$e")
            }
        }
    }
}

/*
    // 3. Print `abc`
    val contents = File(dir + "/abc").readText()
    println("ИГР GitC.setup-03 contents: '$contents'")
*/

/*
fun gitListFiles(dir: String) {
    val files = File(dir).listFiles()
    println("ИГР gitLF-01 dir/files: '$dir'")
    for (item in files) {
        println("ИГР > '$item'")
    }
    println("ИГР gitLF-02 dir: '$dir'")
}
*/

fun gitLocateRootDir(ctx: Context) {
    val dir = ctx.getExternalFilesDir(null)?.absolutePath ?: "N/A"
    gitSet(F.rootDir, dir)
}

fun gitPull(dir: String) {
    val cr = UsernamePasswordCredentialsProvider(
        "kornerr@gmail.com",
        hash
    )
    val client = Git
        .open(File(dir))
        .pull()
        .setCredentialsProvider(cr)

    GlobalScope.launch(Dispatchers.IO) {
        try {
            client.call()
            GlobalScope.launch(Dispatchers.Main) {
                gitSet(F.didPull, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalScope.launch(Dispatchers.Main) {
                gitSet(F.pullError, "$e")
            }
        }
    }
}
