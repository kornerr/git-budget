package org.opengamestudio

import java.time.LocalDate
import java.time.temporal.ChronoField

// Date of the report (yesterday) as YYYY-MM-DD
fun logReportedDateYear(): String {
    val now = LocalDate.now()
    val yesterday = now.minusDays(1)
    val day = yesterday.get(ChronoField.DAY_OF_MONTH)
    val mon = yesterday.get(ChronoField.MONTH_OF_YEAR)
    val year = yesterday.get(ChronoField.YEAR)
    val sday = day.toString().padStart(2, '0')
    val smon = mon.toString().padStart(2, '0')
    val syear = year.toString()
    return "$syear-$smon-$sday"
}
