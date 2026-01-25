package org.opengamestudio
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object VM {
    var androidContext: Context? = null

    val inputMorningBalance = mutableStateOf("")
    val inputMorningBalanceLabel = mutableStateOf("Morning balance")
    val inputMorningBalancePasteTitle = mutableStateOf("Paste")
    val inputMorningBalancePlaceholder = mutableStateOf("TODO-Morning-balance")

    val inputSpent = mutableStateOf("")
    val inputSpentLabel = mutableStateOf("Spent")
    val inputSpentPasteTitle = mutableStateOf("Paste")
    val inputSpentPlaceholder = mutableStateOf("TODO-Spent")

    val result = mutableStateOf("TODO-Result")
    val resultCopyTitle = mutableStateOf("Copy")

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
