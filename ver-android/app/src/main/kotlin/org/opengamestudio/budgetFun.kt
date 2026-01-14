package org.opengamestudio
import kotlin.math.abs

//<!-- Constants -->

val BUDGET_INITIAL_SUM = 30000f
val BUDGET_RESTDAY_SUM = 15000f
val BUDGET_RESULT_DATE_T = "%DATE%"
val BUDGET_RESULT_LEFT_T = "Осталось Р/д: %VALUE%"
val BUDGET_RESULT_OVERRUN_T = "Перерасход: %VALUE%"
val BUDGET_RESULT_WEEKDAY_T = "Будни: %SPENT% / %BALANCE% %PERCENT%"
val BUDGET_RESULT_WEEKEND_T = "Выходные: %SPENT% / %BALANCE% %PERCENT%"
val BUDGET_WEEKDAY_MON = 1
val BUDGET_WEEKDAY_TUE = 2
val BUDGET_WEEKDAY_WED = 3
val BUDGET_WEEKDAY_THU = 4
val BUDGET_WEEKDAY_FRI = 5
val BUDGET_WEEKDAY_SAT = 6
val BUDGET_WEEKDAY_SUN = 7
val BUDGET_WORKDAY_SUM = 6000f

//<!-- Shoulds -->

/* Construct result
 *
 * Conditions:
 * 1. Did launch or specified spent/balance
 */
fun budgetShouldResetResult(c: BudgetContext): BudgetContext {
    if (
        c.recentField == "didLaunch" ||
        c.recentField == "inputMorningBalance" ||
        c.recentField == "spent"
    ) {
        val mbalance = budgetNumber(budgetStringOnlyNumerical(c.inputMorningBalance))
        val spent = budgetNumber(budgetStringOnlyNumerical(c.spent))
        var lines = arrayOf<String>()
        lines += budgetResultDate(c.reportedDate)
        lines += budgetResultSpent(mbalance, c.reportedWeekday, spent)
        lines += budgetResultOverrun(mbalance, c.reportedWeekday, spent)
        lines += budgetResultLeft(mbalance, c.reportedWeekday, spent)
        c.result = lines.joinToString("\n")
        c.recentField = "result"
        return c
    }

    c.recentField = "none"
    return c
}

/* Consolidate spent value
 *
 * Conditions:
 * 1. User did input spent value
 * 2. User did paste spent value
 */
fun budgetShouldResetSpent(c: BudgetContext): BudgetContext {
    /* 1 */ if (c.recentField == "inputSpent") {
        c.spent = c.inputSpent
        c.recentField = "spent"
        return c
    }

    /* 2 */ if (c.recentField == "pastedSpent") {
        c.spent = c.pastedSpent
        c.recentField = "spent"
        return c
    }

    c.recentField = "none"
    return c
}

//<!-- Other functions -->

// Остаток виден не во все дни. Не виден в:
// 1. пятницу
// 2. воскресенье
// т.к. в при отчёте за эти дни в эти самые дни уже закончился
// бюджет половины, начался новый бюджет
fun budgetIsLeftVisible(reportedWeekday: Int): Boolean {
    /* 1 */ if (
        reportedWeekday >= BUDGET_WEEKDAY_MON &&
        reportedWeekday < BUDGET_WEEKDAY_FRI
    ) {
        return true
    }

    /* 2 */ if (reportedWeekday == BUDGET_WEEKDAY_SAT) {
        return true
    }

    return false
}


// Выходной ли в отчётный день?
fun budgetIsWeekend(reportedWeekday: Int): Boolean {
    return reportedWeekday == BUDGET_WEEKDAY_SAT ||
        reportedWeekday == BUDGET_WEEKDAY_SUN
}

// Привести строку к Float
fun budgetNumber(s: String): Float {
    // Заменяем запятую на точку
    val dotted = s.replace(",", ".")
    // Переводим в число
    val almost = dotted.toFloatOrNull()
    return almost ?: 0f
}

// Ограничить двумя цифрами после запятой
fun budgetStringNumber(
    value: Float,
    digitsCount: Int
): String {
    val str = "$value"
    val parts = str.split(".")

    // Дробное число без чисел после запятой
    if (
        parts.size == 2 &&
        parts[1]!!.length > 0 &&
        digitsCount == 0
    ) {
        val integer = parts[0]!!
        return "$integer"
    }

    // Дробное число с количеством чисел после запятой не больше digitsCount
    if (
        parts.size == 2 &&
        parts[1]!!.length > digitsCount
    ) {
        val integer = parts[0]!!
        val fraction = parts[1]!!.substring(0, digitsCount)
        return "$integer.$fraction"
    }

    return str
}

// Исключаем всё, что не про число
fun budgetStringOnlyNumerical(str: String): String {
    var nums = ""
    for (char in str) {
        if (
            char.isDigit() ||
            char == ',' ||
            char == '.' ||
            char == '-'
        ) {
            nums += char
        }
    }

    return nums
}

// Отчётная дата
fun budgetResultDate(reportedDate: String): String {
    return BUDGET_RESULT_DATE_T.replace("%DATE%", reportedDate)
}

// Отсталось Р/д
fun budgetResultLeft(
    morningBalance: Float,
    reportedWeekday: Int,
    spent: Float
): String {
    val todayBalance = morningBalance - spent

    if (!budgetIsLeftVisible(reportedWeekday)) {
        return ""
    }

    // Если ушли в минус, то ничего не осталось
    if (todayBalance < 0) {
        return BUDGET_RESULT_LEFT_T.replace("%VALUE%", "0")
    }

    // Будни (без пт)
    if (!budgetIsWeekend(reportedWeekday)) {
        val daysLeft = 5 - reportedWeekday
        val left = todayBalance / daysLeft
        val sleft = budgetStringNumber(left, 0)
        return BUDGET_RESULT_LEFT_T.replace("%VALUE%", sleft)
    }

    // Выходные (без вс)
    // Нужны данные за две половины, а их пока нет
    return ""
}

// Перерасход
fun budgetResultOverrun(
    morningBalance: Float,
    reportedWeekday: Int,
    spent: Float
): String {
    val todayBalance = morningBalance - spent
    val targetBalance = budgetTargetMorningBalance(reportedWeekday)
    if (todayBalance < targetBalance) {
        val diff = targetBalance - todayBalance
        val sdiff = budgetStringNumber(diff, 2)
        return BUDGET_RESULT_OVERRUN_T.replace("%VALUE%", sdiff)
    }

    return ""
}

// Потрачено / баланс процент
fun budgetResultSpent(
    morningBalance: Float,
    reportedWeekday: Int,
    spent: Float
): String {
    // Выбор шаблона weekday или weekend
    var weekT = BUDGET_RESULT_WEEKDAY_T
    if (
        reportedWeekday == BUDGET_WEEKDAY_SAT ||
        reportedWeekday == BUDGET_WEEKDAY_SUN
    ) {
        weekT = BUDGET_RESULT_WEEKEND_T
    }

    // Потрачено / баланс процент
    val balance = morningBalance - spent
    val balanceStr = budgetStringNumber(balance, 2)
    val percent = balance * 100f / BUDGET_INITIAL_SUM
    val percentStr = budgetStringNumber(percent, 0)
    return weekT
            .replace("%SPENT%", "$spent")
            .replace("%BALANCE%", balanceStr)
            .replace("%PERCENT%", "$percentStr%")
}

// Ожидаемый (без превышения) размер утреннего баланса
fun budgetTargetMorningBalance(reportedWeekday: Int): Float {
    // Будни
    if (
        reportedWeekday >= BUDGET_WEEKDAY_MON &&
        reportedWeekday < BUDGET_WEEKDAY_SAT
    ) {
        return BUDGET_INITIAL_SUM - reportedWeekday * BUDGET_WORKDAY_SUM
    }
    // Выходные
    return BUDGET_INITIAL_SUM - (reportedWeekday - 5) * BUDGET_RESTDAY_SUM
}
