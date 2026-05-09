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
    external fun getCurrentDataContextDidLaunch(): Boolean
    external fun getCurrentDataContextSelectedId(): Int
    external fun getCurrentDataContextUrl(): String
}
