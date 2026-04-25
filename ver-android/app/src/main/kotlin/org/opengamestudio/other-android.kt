package org.opengamestudio

import android.app.AlertDialog
import android.content.Context
import java.io.*
import java.net.*
import kotlinx.coroutines.*

val OTHER_FAILURE_OK = "OK"

// Load data over HTTP(s)
// https://johncodeos.com/post-get-put-delete-requests-with-httpurlconnection/
fun loadURL(
    p: NetRequest,
    onload: (NetResponse) -> Unit,
    onerror: (NetResponse) -> Unit,
) {
    // https://stackoverflow.com/a/71004286/3404710
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        var err = NetResponse()
        err.req = p
        // https://stackoverflow.com/a/65437800/3404710
        GlobalScope.launch(Dispatchers.Main) {
            /**/println("ИГР loadU-1 exc onerror")
            onerror(err)
        }
    }

    GlobalScope.launch(Dispatchers.IO + exceptionHandler) {
        val url = URL(p.url)
        val cn = url.openConnection() as HttpURLConnection
        cn.requestMethod = p.method
        cn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        cn.doInput = true
        if (!p.body.isEmpty()) {
            cn.doOutput = true
            val body = OutputStreamWriter(cn.outputStream)
            body.write(p.body)
            body.flush()
        }

        var resp = NetResponse()
        resp.req = p
        if (cn.responseCode == HttpURLConnection.HTTP_OK) {
            resp.contents = cn.inputStream.bufferedReader().use { it.readText() }
            withContext(Dispatchers.Main) {
                /**/println("ИГР loadU-2 onload")
                onload(resp)
            }
        } else {
            // https://stackoverflow.com/a/613484/3404710
            resp.contents = cn.errorStream.bufferedReader().use { it.readText() }
            withContext(Dispatchers.Main) {
                /**/println("ИГР loadU-3 onerror")
                onerror(resp)
            }
        }
    }
}

// Use modal dialog to report a failure
fun reportFailure(
    context: Context,
    title: String,
    message: String
) {
    val builder = AlertDialog.Builder(context)
    builder
        .setTitle(title)
        .setMessage("Error: '$message'")
        .setPositiveButton(OTHER_FAILURE_OK) { dialog, id -> }
    val dialog = builder.create()
    dialog.show()
}
