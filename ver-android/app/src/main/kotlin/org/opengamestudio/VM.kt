package org.opengamestudio
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object VM {
    var androidContext: Context? = null

    val inputMorningBalance = mutableStateOf("")
    val inputMorningBalancePasteTitle = mutableStateOf("Paste")
    val inputMorningBalancePlaceholder = mutableStateOf("Morning balance")

    val inputSpent = mutableStateOf("")
    val inputSpentPasteTitle = mutableStateOf("Paste")
    val inputSpentPlaceholder = mutableStateOf("Spent")

    val result = mutableStateOf("TODO-Result")
    val resultCopyTitle = mutableStateOf("Copy")

    fun reportFailure(
        title: String,
        message: String
    ) {
        reportFailure(androidContext!!, title, message)
    }
}
