/**
 * This file is a part of Cross-language dialect:
 *     https://github.com/OGStudio/cross-language-dialect
 * License: CC0
 * Version: 1.2.0
 */

package org.opengamestudio

interface CLDContext {
    /**
     * Name of the field that has just changed
     *
     * Allows shoulds/handlers/behaviour functions to react to only
     * relevant changes and ignore other changes of CLDContext
     */
    var recentField: String

    /**
     * Get field's value by its name
     */
    fun <T> field(name: String): T
    /**
     * Erase type
     *
     * Used by CLDController to assign recent field's value
     */
    fun fieldAny(name: String): Any {
        return field(name)
    }
    /**
     * Create a copy of the CLDContext derivative
     *
     * Used by CLDController to treat all derived contexts as CLDContext
     */
    fun selfCopy(): CLDContext
    /**
     * Set field's value by its name
     */
    fun setField(name: String, value: Any?)
}
