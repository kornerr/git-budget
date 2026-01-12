package org.opengamestudio

//<!-- Constants -->

//val AUTH_API_LOGIN = "/api/rest/login"

//<!-- Shoulds -->

/* Perform network request
 *
 * Conditions:
 * 1. User clicked ...
 */

fun budgetShouldLoad(c: BudgetContext): BudgetContext {
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

//<!-- Other functions -->

