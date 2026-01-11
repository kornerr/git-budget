package org.opengamestudio

//<!-- Constants -->

//val AUTH_API_LOGIN = "/api/rest/login"

//<!-- Shoulds -->

/* Perform network request
 *
 * Conditions:
 * 1. User clicked ...
 */

fun playShouldLoad(c: PlayContext): PlayContext {
  /*
    if (c.recentField == "didClickContinue") {
        c.request =
            NetRequest(
                "",
                "GET",
                authURL(c.inputHost, AUTH_API_SYSTEM_INFO),
            )
        c.recentField = "request"
        return c
    }
    */

    c.recentField = "none"
    return c
}

/* Set URL for WebView
 *
 * Conditions:
 * 1. Did reset web view for the first time
 */
fun playShouldResetPlaygroundURL(c: PlayContext): PlayContext {
    if (
        c.recentField == "didResetWebView" &&
        c.url.isEmpty()
    ) {
        c.url = "https://kornerr.ru/dbg/budget.html"
        c.recentField = "url"
        return c
    }

    c.recentField = "none"
    return c
}

/* Set playground screen visibility
 *
 * Conditions:
 * 1. Did launch
 */
fun playShouldResetPlaygroundVisibility(c: PlayContext): PlayContext {
    if (c.recentField == "didLaunch") {
        c.isPlaygroundVisible = true
        c.recentField = "isPlaygroundVisible"
        return c
    }

    c.recentField = "none"
    return c
}

//<!-- Other functions -->

