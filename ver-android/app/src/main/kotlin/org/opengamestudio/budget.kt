package org.opengamestudio
import android.content.ClipboardManager
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
            "pasteSpent", { c: BC -> budgetPasteSpent(vm) },
            "result", { c: BC -> vm.result.value = c.result },
        )
        registerOneliners(ctrl, oneliners)
    }

    fun setupShoulds() {
        arrayOf(
          ::budgetShouldResetResult,
        ).forEach { f ->
          ctrl.registerFunction { c -> f(c as BudgetContext) }
        }
    }
}

//<!-- Effects -->

fun budgetPasteSpent(vm: VM) {
    val clipboardManager = VM.androidContext!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = clipboardManager.primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        val clipboardText = clipData.getItemAt(0).text.toString()
        budgetCtrl().set("pastedSpent", clipboardText)
    }
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
