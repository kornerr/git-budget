package org.opengamestudio.checklib

// Detailed instructions here https://docs.swifdroid.com/lib/kotlin-project

object SwiftInterface {
    init {
        System.loadLibrary("Checklib")
    }

    external fun initialize(caller: Any)

    external fun sendAny(key: String, value: Any)
    external fun getCurrentDataContext(): DataContextDto
    fun interface DataContextCallback {
        fun onChanged()
    }
    external fun registerCallback(callback: DataContextCallback)
}

data class DataContextDto(
    val didLaunch: Boolean,
    val selectedId: Int,
    val url: String,
    val recentField: String
)
