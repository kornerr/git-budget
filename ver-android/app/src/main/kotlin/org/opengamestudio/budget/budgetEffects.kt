package org.opengamestudio

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context

fun budgetCopyResult(
    ctx: Context,
    result: String
) {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val item = ClipData.newPlainText("Result", result)
    clip.setPrimaryClip(item)
}

fun budgetPasteMorningBalance(ctx: Context) {
    val txt = budgetClipboardText(ctx) ?: ""
    budgetSet(F.pastedMorningBalance, txt)
}

fun budgetPasteSpent(ctx: Context) {
    val txt = budgetClipboardText(ctx) ?: ""
    budgetSet(F.pastedSpent, txt)
}
