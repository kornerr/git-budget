package org.opengamestudio

fun logParse(lines: Array<String>) {
    val items = lines
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .chunked(3)
        .filter { group ->
            group.size == 3 &&
            group[0].startsWith("DA ") &&
            group[1].startsWith("SP ") &&
            group[2].startsWith("LE ")
        }
        .map { group ->
            LogItem(
                dt = group[0].removePrefix("DA ").replace("-", "").toInt(),
                spent = group[1].removePrefix("SP ").toFloat(),
                left = group[2].removePrefix("LE ").toFloat()
            )
        }
        .toTypedArray()
    logSet(F.items, items)
}
