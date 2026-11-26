package org.opengamestudio

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context

fun budgCopyResult(
    ctx: Context,
    result: String
) {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val item = ClipData.newPlainText("Result", result)
    clip.setPrimaryClip(item)
}

fun budgPasteMorningBalance(ctx: Context) {
    val txt = clipboardText(ctx)
    budgSet(F.pastedMorningBalance, txt)
}

fun budgPasteSpent(ctx: Context) {
    val txt = clipboardText(ctx)
    budgSet(F.pastedSpent, txt)
}
