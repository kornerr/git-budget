package stream.swift.droid.checklib

import java.util.Date

// Detailed instructions here https://docs.swifdroid.com/lib/kotlin-project

object SwiftInterface {
    init {
        System.loadLibrary("Checklib")
    }

    external fun initialize(caller: Any)

}