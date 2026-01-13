/**
 * This file is a part of Cross-language dialect:
 *     https://github.com/OGStudio/cross-language-dialect
 * License: CC0
 * Version: 1.2.0
 */

package org.opengamestudio

// Register several oneliner callbacks to a controller
fun registerOneliners(
    ctrl: CLDController,
    items: Array<Any>
) {
    val halfCount = items.size / 2
    for (i in 0..<halfCount) {
        val field = items[i * 2] as String
        val callback = items[i * 2 + 1] as (c: CLDContext) -> Unit
        ctrl.registerFieldCallback(field, callback);
    }
}
