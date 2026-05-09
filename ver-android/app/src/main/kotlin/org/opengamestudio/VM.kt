package org.opengamestudio
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object VM {
    var androidContext: Context? = null

    val commitTitle = mutableStateOf("Commit")

    val morningBalance = mutableStateOf("")
    val morningBalancePasteTitle = mutableStateOf("Paste")
    val morningBalancePlaceholder = mutableStateOf("Morning balance")

    val pushTitle = mutableStateOf("Push")

    val result = mutableStateOf("TODO-Result")
    val resultCopyTitle = mutableStateOf("Copy")

    val spent = mutableStateOf("")
    val spentPasteTitle = mutableStateOf("Paste")
    val spentPlaceholder = mutableStateOf("Spent")
}
