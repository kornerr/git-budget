package org.opengamestudio



data class BudgetContext(
    var copyResult: Boolean = false,
    var didClickPaste: Boolean = false,
    var didLaunch: Boolean = false,
    var inputDate: String = "",
    var inputMorningBalance: String = "",
    var inputSpent: String = "",
    var pasteMorningBalance: Boolean = false,
    var pastedSpent: String = "",
    var reportedDate: String = "",
    var reportedWeekday: Int = 0,
    var result: String = "",
    override var recentField: String = "",
): CLDContext {
    override fun <T> field(name: String): T {
        if (name == "copyResult") {
            return copyResult as T
        } else if (name == "didClickPaste") {
            return didClickPaste as T
        } else if (name == "didLaunch") {
            return didLaunch as T
        } else if (name == "inputDate") {
            return inputDate as T
        } else if (name == "inputMorningBalance") {
            return inputMorningBalance as T
        } else if (name == "inputSpent") {
            return inputSpent as T
        } else if (name == "pasteMorningBalance") {
            return pasteMorningBalance as T
        } else if (name == "pastedSpent") {
            return pastedSpent as T
        } else if (name == "reportedDate") {
            return reportedDate as T
        } else if (name == "reportedWeekday") {
            return reportedWeekday as T
        } else if (name == "result") {
            return result as T
        }
        return "unknown-field-name" as T
    }

    override fun selfCopy(): CLDContext {
        return this.copy()
    }

    override fun setField(
        name: String,
        value: Any?
    ) {
        if (name == "copyResult") {
            copyResult = value as Boolean
        } else if (name == "didClickPaste") {
            didClickPaste = value as Boolean
        } else if (name == "didLaunch") {
            didLaunch = value as Boolean
        } else if (name == "inputDate") {
            inputDate = value as String
        } else if (name == "inputMorningBalance") {
            inputMorningBalance = value as String
        } else if (name == "inputSpent") {
            inputSpent = value as String
        } else if (name == "pasteMorningBalance") {
            pasteMorningBalance = value as Boolean
        } else if (name == "pastedSpent") {
            pastedSpent = value as String
        } else if (name == "reportedDate") {
            reportedDate = value as String
        } else if (name == "reportedWeekday") {
            reportedWeekday = value as Int
        } else if (name == "result") {
            result = value as String
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
