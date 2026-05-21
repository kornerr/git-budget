package org.opengamestudio

// Print changed key/value pair in a uniform way
fun formatContextChange(
    c: KDContext,
    prefix: String
): String {
    val fullValue = "${c.field<String>(c.recentField)}"
    val shortValue = fullValue.take(100)
    return "$prefix k: '${c.recentField}' v: '$shortValue'"
}

// Print each key/value processed by a component into console
fun setupComponentDebugging(
    ctrl: KDController,
    prefix: String
) {
    ctrl.registerCallback { c ->
        val value = "${c.field<String>(c.recentField)}"
        println("ИГР $prefix k/v: '${c.recentField}'/'$value'")
    }
}
