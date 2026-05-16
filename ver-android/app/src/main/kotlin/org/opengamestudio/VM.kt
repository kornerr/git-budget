package org.opengamestudio

import android.content.Context
import androidx.compose.runtime.*

object VM {
    var androidContext: Context? = null

    val commitTitle = mutableStateOf("Commit")

    val isBudgetTabSelected = mutableStateOf(false)
    val isSettingsTabSelected = mutableStateOf(false)

    val morningBalance = mutableStateOf("")
    val morningBalancePasteTitle = mutableStateOf("Paste")
    val morningBalancePlaceholder = mutableStateOf("Morning balance")

    val pushTitle = mutableStateOf("Push")

    val result = mutableStateOf("TODO-Result")
    val resultCopyTitle = mutableStateOf("Copy")

    val spent = mutableStateOf("")
    val spentPasteTitle = mutableStateOf("Paste")
    val spentPlaceholder = mutableStateOf("Spent")

    val stubItems = mutableStateListOf(
        "Account", "Notifications", "Privacy", "Security", "Appearance",
        "Language", "Storage", "Backup", "About", "Help",
        "Advanced", "Developer Options", "Beta Features", "Experimental", "Network",
        "Proxy", "Cache", "Sync", "Theme", "Font Size",
    )
}
