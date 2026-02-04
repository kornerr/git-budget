package org.opengamestudio

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import java.io.*
import java.net.*
import kotlinx.coroutines.*

val OTHER_FAILURE_OK = "OK"

// Get clipboard text (of the first item)
fun clipboardText(ctx: Context): String? {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clip.getPrimaryClip()?.getItemAt(0)?.getText().toString()
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
