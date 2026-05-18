package org.opengamestudio

fun budgHistoryBalance(
    historyItems: Map<String, HistItem>,
    dt: String,
    weekday: Int
): String {
    if (
        weekday == BUDG_WEEKDAY_SUN ||
        weekday == BUDG_WEEKDAY_FRI
    ) {
        return BUDG_INITIAL_SUM.toString()
    }

    return historyItems[dt]?.left.toString() ?: "0000"
}

// Остаток виден не во все дни. Не виден в:
// 1. пятницу
// 2. воскресенье
// т.к. в при отчёте за эти дни в эти самые дни уже закончился
// бюджет половины, начался новый бюджет
fun budgIsLeftVisible(reportedWeekday: Int): Boolean {
    /* 1 */ if (
        reportedWeekday >= BUDG_WEEKDAY_MON &&
        reportedWeekday < BUDG_WEEKDAY_FRI
    ) {
        return true
    }

    /* 2 */ if (reportedWeekday == BUDG_WEEKDAY_SAT) {
        return true
    }

    return false
}

// Выходной ли в отчётный день?
fun budgIsWeekend(reportedWeekday: Int): Boolean {
    return reportedWeekday == BUDG_WEEKDAY_SAT ||
        reportedWeekday == BUDG_WEEKDAY_SUN
}

// Привести строку к Float
fun budgNumber(s: String): Float {
    // Заменяем запятую на точку
    val dotted = s.replace(",", ".")
    // Переводим в число
    val almost = dotted.toFloatOrNull()
    return almost ?: 0f
}

// Ограничить двумя цифрами после запятой
fun budgStringNumber(
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
fun budgStringOnlyNumerical(str: String): String {
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
fun budgResultDate(reportedDate: String): String {
    return BUDG_RESULT_DATE_T.replace("%DATE%", reportedDate)
}

// Отсталось Р/д
fun budgResultLeft(
    morningBalance: Float,
    reportedWeekday: Int,
    spent: Float
): String {
    val todayBalance = morningBalance - spent

    if (!budgIsLeftVisible(reportedWeekday)) {
        return ""
    }

    // Если ушли в минус, то ничего не осталось
    if (todayBalance < 0) {
        return BUDG_RESULT_LEFT_T.replace("%VALUE%", "0")
    }

    // Будни (без пт)
    if (!budgIsWeekend(reportedWeekday)) {
        val daysLeft = 5 - reportedWeekday
        val left = todayBalance / daysLeft
        val sleft = budgStringNumber(left, 0)
        return BUDG_RESULT_LEFT_T.replace("%VALUE%", sleft)
    }

    // Выходные (без вс)
    // Нужны данные за две половины, а их пока нет
    return ""
}

// Перерасход
fun budgResultOverrun(
    morningBalance: Float,
    reportedWeekday: Int,
    spent: Float
): String {
    val todayBalance = morningBalance - spent
    val targetBalance = budgTargetMorningBalance(reportedWeekday)
    if (todayBalance < targetBalance) {
        val diff = targetBalance - todayBalance
        val sdiff = budgStringNumber(diff, 2)
        return BUDG_RESULT_OVERRUN_T.replace("%VALUE%", sdiff)
    }

    return ""
}

// Потрачено / баланс процент
fun budgResultSpent(
    morningBalance: Float,
    reportedWeekday: Int,
    spent: Float
): String {
    // Выбор шаблона weekday или weekend
    var weekT = BUDG_RESULT_WEEKDAY_T
    if (
        reportedWeekday == BUDG_WEEKDAY_SAT ||
        reportedWeekday == BUDG_WEEKDAY_SUN
    ) {
        weekT = BUDG_RESULT_WEEKEND_T
    }

    // Потрачено / баланс процент
    val balance = morningBalance - spent
    val balanceStr = budgStringNumber(balance, 2)
    val percent = balance * 100f / BUDG_INITIAL_SUM
    val percentStr = budgStringNumber(percent, 0)
    return weekT
            .replace("%SPENT%", "$spent")
            .replace("%BALANCE%", balanceStr)
            .replace("%PERCENT%", "$percentStr%")
}

// Ожидаемый (без превышения) размер утреннего баланса
fun budgTargetMorningBalance(reportedWeekday: Int): Float {
    // Будни
    if (
        reportedWeekday >= BUDG_WEEKDAY_MON &&
        reportedWeekday < BUDG_WEEKDAY_SAT
    ) {
        return BUDG_INITIAL_SUM - reportedWeekday * BUDG_WORKDAY_SUM
    }
    // Выходные
    return BUDG_INITIAL_SUM - (reportedWeekday - 5) * BUDG_RESTDAY_SUM
}
