package org.opengamestudio

import android.content.ClipboardManager
import android.content.Context
import java.time.LocalDate
import java.time.temporal.ChronoField

// Get clipboard text
fun budgetClipboardText(ctx: Context): String? {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clip.getPrimaryClip()?.getItemAt(0)?.getText().toString()
}

// Date of the report (yesterday)
fun budgetReportedDate(): String {
    val now = LocalDate.now()
    val yesterday = now.minusDays(1)
    val day = yesterday.get(ChronoField.DAY_OF_MONTH)
    val mon = yesterday.get(ChronoField.MONTH_OF_YEAR)
    val sday = day.toString().padStart(2, '0')
    val smon = mon.toString().padStart(2, '0')
    return "$sday.$smon"
}

// Reported week day
// 1 == Monday, ..., 7 == Sunday
fun budgetReportedWeekday(): Int {
    val now = LocalDate.now()
    val yesterday = now.minusDays(1)
    return yesterday.get(ChronoField.DAY_OF_WEEK)
}
