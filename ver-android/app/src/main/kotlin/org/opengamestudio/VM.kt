package org.opengamestudio
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object VM {
    var androidContext: Context? = null

    val morningBalance = mutableStateOf("")
    val morningBalancePasteTitle = mutableStateOf("Paste")
    val morningBalancePlaceholder = mutableStateOf("Morning balance")

    val result = mutableStateOf("TODO-Result")
    val resultCopyTitle = mutableStateOf("Copy")

    val spent = mutableStateOf("")
    val spentPasteTitle = mutableStateOf("Paste")
    val spentPlaceholder = mutableStateOf("Spent")

    fun reportFailure(
        title: String,
        message: String
    ) {
        reportFailure(androidContext!!, title, message)
    }
}
