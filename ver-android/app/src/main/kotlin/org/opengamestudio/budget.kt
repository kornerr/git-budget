package org.opengamestudio

import java.time.LocalDate
import java.time.temporal.ChronoField
/**/import java.util.Calendar

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
        /*
        val oneliners = arrayOf(
            "isPlaygroundVisible", { c: BC -> vm.playgroundIsVisible.value = c.isPlaygroundVisible },
        )
        registerOneliners(ctrl, oneliners)
        */
    }

    fun setupShoulds() {
        /*
        arrayOf(
          ::playShouldResetPlaygroundVisibility,
        ).forEach { f ->
          ctrl.registerFunction { c -> f(c as BudgetContext) }
        }
        */
    }
}

//<!-- Effects -->
 
//<!-- Other functions -->

// Date of the report (yesterday)
fun budgetReportedDate(): String {
    var cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val mon = cal.get(Calendar.MONTH) + 1
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
