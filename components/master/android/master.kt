package org.opengamestudio

/*
import android.content.Context
import java.io.File
import kotlinx.coroutines.*
*/

private typealias MC = MasterContext

object MasterComponent {
    init {
        val oneliners = arrayOf(
            F.dataDir, { c: MC -> masterListFiles(c.dataDir) },
        )
        registerOneliners(budgetCtrl(), oneliners)
    }

    fun setup() {
        masterSet(F.didLaunch, true)
    }
}
