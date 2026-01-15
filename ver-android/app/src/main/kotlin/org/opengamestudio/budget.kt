package org.opengamestudio
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import java.time.LocalDate
import java.time.temporal.ChronoField

//<!-- API -->

fun budgetCtrl(): CLDController {
    return BudgetComponent.ctrl
}

//<!-- Constants -->

//<!-- Component -->

private typealias BC = BudgetContext

object BudgetComponent {
    val ctrl: CLDController

    init {
        ctrl = CLDController(BudgetContext())
        // Debug
        ctrl.registerCallback { c ->
            var value = "${c.field(c.recentField) as Any}"
            println("ИГР BudgetC.init ctrl key/value: '${c.recentField}'/'$value'")
        }

        // Default values
        ctrl.set("reportedDate", budgetReportedDate())
        ctrl.set("reportedWeekday", budgetReportedWeekday())

        setupEffects()
        setupShoulds()
    }

    fun setupEffects() {
        val vm = VM
        val oneliners = arrayOf(
            "didClickCopy", { c: BC -> budgetCopyResult(vm.androidContext!!, c.result) },
            "didClickPaste", { c: BC -> budgetPasteSpent(vm.androidContext!!) },
            "pastedSpent", { c: BC -> vm.inputSpent.value = c.pastedSpent },
            "result", { c: BC -> vm.result.value = c.result },
        )
        registerOneliners(ctrl, oneliners)
    }

    fun setupShoulds() {
        arrayOf(
          ::budgetShouldResetResult,
          ::budgetShouldResetSpent,
        ).forEach { f ->
          ctrl.registerFunction { c -> f(c as BudgetContext) }
        }
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

fun budgetPasteSpent(ctx: Context) {
    val clip = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val txt = clip.getPrimaryClip()?.getItemAt(0)?.getText().toString() ?: "N/A"
    budgetCtrl().set("pastedSpent", txt)
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
