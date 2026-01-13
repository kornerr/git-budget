/**
 * This file is a part of Cross-language dialect:
 *     https://github.com/OGStudio/cross-language-dialect
 * License: CC0
 * Version: 1.2.0
 */

package org.opengamestudio

class CLDController(
    var context: CLDContext
) {
    internal var callbacks = mutableListOf<(c: CLDContext) -> Unit>()
    internal var functions = mutableListOf<(c: CLDContext) -> CLDContext>()
    var isProcessingQueue = false
    internal var queue = mutableListOf<CLDContext>()
 
    fun executeFunctions() {
        val c = queue.removeAt(0)
        context.recentField = c.recentField
        context.setField(c.recentField, c.fieldAny(c.recentField))
       
        for (f in functions) {
            val ctx = f(context.selfCopy())
            if (ctx.recentField != "none") {
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
 
    fun registerCallback(cb: (c: CLDContext) -> Unit) {
        callbacks.add(cb)
    }
 
    fun registerFieldCallback(
        fieldName: String,
        cb: (CLDContext) -> Unit
    ) {
        callbacks.add({ c ->
            if (c.recentField == fieldName) {
                cb(c)
            }
        })
    }
 
    fun registerFunction(f: (CLDContext) -> CLDContext) {
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
