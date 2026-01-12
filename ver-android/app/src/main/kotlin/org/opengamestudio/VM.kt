package org.opengamestudio
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object VM {
    var androidContext: Context? = null
    val inputSpent = mutableStateOf("")
    val inputSpentLabel = mutableStateOf("Spent")
    val inputSpentPlaceholder = mutableStateOf("TODO-Placeholder")

    init {
        budgetCtrl().set("didLaunch", true)
        // Launch main component differently since it has no ctrl.
        MainComponent.setupEffects()
    }

    fun reportFailure(
        title: String,
        message: String
    ) {
        reportFailure(androidContext!!, title, message)
    }
}
