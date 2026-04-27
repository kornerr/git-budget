package org.opengamestudio

import android.content.ClipboardManager
import android.content.Context

// Get clipboard text
fun clipboardText(ctx: Context): String {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clip.getPrimaryClip()?.getItemAt(0)?.getText().toString() ?: ""
}