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
): CLDContext {
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

    override fun selfCopy(): CLDContext {
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
