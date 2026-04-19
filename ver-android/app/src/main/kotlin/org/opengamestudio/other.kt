package org.opengamestudio

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
