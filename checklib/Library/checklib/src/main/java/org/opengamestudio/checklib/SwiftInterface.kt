package org.opengamestudio.checklib

import java.util.Date

// Detailed instructions here https://docs.swifdroid.com/lib/kotlin-project

object SwiftInterface {
    init {
        System.loadLibrary("Checklib")
    }

    external fun initialize(caller: Any)

    external fun sendInt(number: Int)
    external fun sendIntArray(array: IntArray)
    external fun sendString(string: String)
    external fun sendDate(date: Date)
    external fun ping(): String
    
    external fun fetchAsyncData(): String
    fun interface FetchCallback {
        fun onResult(result: String)
    }
    external fun fetchAsyncDataWithCallback(callback: FetchCallback)
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
