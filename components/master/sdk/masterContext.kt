package org.opengamestudio

data class MasterContext(
    var didLaunch: Boolean = false,
    var dataDir: String = "",
    override var recentField: String = "",
): KDContext {
    override fun <T> field(name: String): T {
        if (name == "didLaunch") {
            return didLaunch as T
        } else if (name == "dataDir") {
            return dataDir as T
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
        if (name == "didLaunch") {
            didLaunch = value as Boolean
        } else if (name == "dataDir") {
            dataDir = value as String
        }
    }
}
