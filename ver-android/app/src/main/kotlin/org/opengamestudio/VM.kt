package org.opengamestudio

import android.content.Context
import androidx.compose.runtime.*

object VM {
    var androidContext: Context? = null

    val isBudgetTabSelected = mutableStateOf(false)
    val isSettingsTabSelected = mutableStateOf(false)

    val logs = mutableStateListOf<String>()

    val morningBalance = mutableStateOf("")
    val morningBalancePasteTitle = mutableStateOf("Paste")
    val morningBalancePlaceholder = mutableStateOf("Morning balance")

    val result = mutableStateOf("TODO-Result")
    val resultCopyTitle = mutableStateOf("Copy")

    val saveTitle = mutableStateOf("Save")

    val spent = mutableStateOf("")
    val spentPasteTitle = mutableStateOf("Paste")
    val spentPlaceholder = mutableStateOf("Spent")
}
