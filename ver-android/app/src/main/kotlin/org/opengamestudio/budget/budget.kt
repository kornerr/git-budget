package org.opengamestudio
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import java.time.LocalDate
import java.time.temporal.ChronoField

//<!-- Component -->

private typealias BC = BudgetContext

object BudgetComponent {
    init {
        val vm = VM
        val oneliners = arrayOf(
            F.didClickCopy, { c: BC -> budgetCopyResult(vm.androidContext!!, c.result) },
            F.didClickPasteMorningBalance, { c: BC -> budgetPasteMorningBalance(vm.androidContext!!) },
            F.didClickPasteSpent, { c: BC -> budgetPasteSpent(vm.androidContext!!) },
            F.morningBalance, { c: BC -> vm.morningBalance.value = c.morningBalance },
            F.spent, { c: BC -> vm.spent.value = c.spent },
            F.result, { c: BC -> vm.result.value = c.result },
        )
        registerOneliners(budgetCtrl(), oneliners)

        // Defaults
        budgetSet(F.reportedDate, budgetReportedDate())
        budgetSet(F.reportedWeekday, budgetReportedWeekday())
    }

    fun setup() {
        budgetSet(F.didSetup, true)
    }
}

//<!-- Effects -->

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

//<!-- Other functions -->

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
