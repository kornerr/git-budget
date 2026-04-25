package org.opengamestudio

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context

private fun clipboardText(ctx: Context): String? {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clip.getPrimaryClip()?.getItemAt(0)?.getText().toString()
}

fun budgetCopyResult(
    ctx: Context,
    result: String
) {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val item = ClipData.newPlainText("Result", result)
    clip.setPrimaryClip(item)
}

fun budgetPasteMorningBalance(ctx: Context) {
    val txt = clipboardText(ctx) ?: "N/A"
    budgetSet(F.pastedMorningBalance, txt)
}

fun budgetPasteSpent(ctx: Context) {
    val txt = clipboardText(ctx) ?: "N/A"
    budgetSet(F.pastedSpent, txt)
}
