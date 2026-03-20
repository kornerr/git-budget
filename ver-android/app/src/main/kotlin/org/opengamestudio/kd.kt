package org.opengamestudio



data class BudgetContext(
    var didClickCopy: Boolean = false,
    var didClickPaste: Boolean = false,
    var didClickPasteMorningBalance: Boolean = false,
    var didLaunch: Boolean = false,
    var inputDate: String = "",
    var inputMorningBalance: String = "",
    var inputSpent: String = "",
    var morningBalance: String = "",
    var pastedMorningBalance: String = "",
    var pastedSpent: String = "",
    var reportedDate: String = "",
    var reportedWeekday: Int = 0,
    var result: String = "",
    var spent: String = "",
    override var recentField: String = "",
): KDContext {
    override fun <T> field(name: String): T {
        if (name == "didClickCopy") {
            return didClickCopy as T
        } else if (name == "didClickPaste") {
            return didClickPaste as T
        } else if (name == "didClickPasteMorningBalance") {
            return didClickPasteMorningBalance as T
        } else if (name == "didLaunch") {
            return didLaunch as T
        } else if (name == "inputDate") {
            return inputDate as T
        } else if (name == "inputMorningBalance") {
            return inputMorningBalance as T
        } else if (name == "inputSpent") {
            return inputSpent as T
        } else if (name == "morningBalance") {
            return morningBalance as T
        } else if (name == "pastedMorningBalance") {
            return pastedMorningBalance as T
        } else if (name == "pastedSpent") {
            return pastedSpent as T
        } else if (name == "reportedDate") {
            return reportedDate as T
        } else if (name == "reportedWeekday") {
            return reportedWeekday as T
        } else if (name == "result") {
            return result as T
        } else if (name == "spent") {
            return spent as T
        }
        return "unknown-field-name" as T
    }

    override fun selfCopy(): KDContext {
        return this.copy()
    }

    override fun setField(
        name: String,
        value: Any?
    ) {
        if (name == "didClickCopy") {
            didClickCopy = value as Boolean
        } else if (name == "didClickPaste") {
            didClickPaste = value as Boolean
        } else if (name == "didClickPasteMorningBalance") {
            didClickPasteMorningBalance = value as Boolean
        } else if (name == "didLaunch") {
            didLaunch = value as Boolean
        } else if (name == "inputDate") {
            inputDate = value as String
        } else if (name == "inputMorningBalance") {
            inputMorningBalance = value as String
        } else if (name == "inputSpent") {
            inputSpent = value as String
        } else if (name == "morningBalance") {
            morningBalance = value as String
        } else if (name == "pastedMorningBalance") {
            pastedMorningBalance = value as String
        } else if (name == "pastedSpent") {
            pastedSpent = value as String
        } else if (name == "reportedDate") {
            reportedDate = value as String
        } else if (name == "reportedWeekday") {
            reportedWeekday = value as Int
        } else if (name == "result") {
            result = value as String
        } else if (name == "spent") {
            spent = value as String
        }
    }
}



data class NetRequest(
    var body: String = "",
    var method: String = "",
    var url: String = "",
) {}



data class NetResponse(
    var contents: String = "",
    var req: NetRequest = NetRequest(),
) {}
/**
 * This file is a part of Kotlin dialect:
 *     https://github.com/OGStudio/kotlin-dialect
 * License: CC0
 * Version: 2.0.0
 */






interface KDContext {
    /**
     * Name of the field that has just been changed
     *
     * Allows should-functions (reducers) to react only to
     * relevant changes and ignore other changes of KDContext
     */
    var recentField: String

    /**
     * Get field's value by its name
     */
    fun <T> field(name: String): T
    /**
     * Erase type
     *
     * Used by KDController to assign recent field's value
     */
    fun fieldAny(name: String): Any {
        return field(name)
    }
    /**
     * Create a copy of the KDContext derivative
     *
     * Used by KDController to treat all derived contexts as KDContext
     */
    fun selfCopy(): KDContext
    /**
     * Set field's value by its name
     */
    fun setField(name: String, value: Any?)
}
/**
 * This file is a part of Kotlin dialect:
 *     https://github.com/OGStudio/kotlin-dialect
 * License: CC0
 * Version: 2.0.0
 */





val KD_FIELD_NONE = "none"


class KDController(
    var context: KDContext
) {
    internal var callbacks = mutableListOf<(c: KDContext) -> Unit>()
    internal var functions = mutableListOf<(c: KDContext) -> KDContext>()
    var isProcessingQueue = false
    internal var queue = mutableListOf<KDContext>()
 
    fun executeFunctions() {
        val c = queue.removeAt(0)
        context.recentField = c.recentField
        context.setField(c.recentField, c.fieldAny(c.recentField))
       
        for (f in functions) {
            val ctx = f(context.selfCopy())
            if (ctx.recentField != KD_FIELD_NONE) {
                queue.add(ctx)
            }
        }
       
        reportContext()
    }
 
    fun processQueue() {
        // Prevent recursion.
        if (isProcessingQueue) {
            return
        }
       
        isProcessingQueue = true
       
        while (queue.size > 0) {
            executeFunctions()
        }
       
        isProcessingQueue = false
    }
 
    fun registerCallback(cb: (c: KDContext) -> Unit) {
        callbacks.add(cb)
    }
 
    fun registerFieldCallback(
        fieldName: String,
        cb: (KDContext) -> Unit
    ) {
        callbacks.add({ c ->
            if (c.recentField == fieldName) {
                cb(c)
            }
        })
    }
 
    fun registerFunction(f: (KDContext) -> KDContext) {
        functions.add(f)
    }
 
    fun reportContext() {
        for (cb in callbacks) {
            cb(context)
        }
    }
 
    fun set(fieldName: String, value: Any) {
        var c = context.selfCopy()
        c.setField(fieldName, value)
        c.recentField = fieldName
        queue.add(c)
        processQueue()
    }
}
/**
 * This file is a part of Kotlin dialect:
 *     https://github.com/OGStudio/kotlin-dialect
 * License: CC0
 * Version: 2.0.0
 */





// Register several oneliner callbacks to a controller

fun registerOneliners(
    ctrl: KDController,
    items: Array<Any>
) {
    val halfCount = items.size / 2
    for (i in 0..<halfCount) {
        val field = items[i * 2] as String
        val callback = items[i * 2 + 1] as (c: KDContext) -> Unit
        ctrl.registerFieldCallback(field, callback);
    }
}

// Special object to reference context fields with a compile time validation

object F {
    const val didClickCopy = "didClickCopy"
    const val didClickPaste = "didClickPaste"
    const val didClickPasteMorningBalance = "didClickPasteMorningBalance"
    const val didLaunch = "didLaunch"
    const val inputDate = "inputDate"
    const val inputMorningBalance = "inputMorningBalance"
    const val inputSpent = "inputSpent"
    const val morningBalance = "morningBalance"
    const val none = "none"
    const val pastedMorningBalance = "pastedMorningBalance"
    const val pastedSpent = "pastedSpent"
    const val reportedDate = "reportedDate"
    const val reportedWeekday = "reportedWeekday"
    const val result = "result"
    const val spent = "spent"

}
