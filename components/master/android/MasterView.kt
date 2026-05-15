package org.opengamestudio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

enum class Tab {
    BUDGET,
    GIT
}

@Composable
fun MasterView(
    modifier: Modifier = Modifier,
    vm: VM,
) {
    var selectedTab by remember { mutableStateOf(Tab.BUDGET) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == Tab.BUDGET,
                    onClick = { selectedTab = Tab.BUDGET },
                    label = { Text("Budget") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Budget") }
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.GIT,
                    onClick = { selectedTab = Tab.GIT },
                    label = { Text("Settings") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            Tab.BUDGET -> BudgetView(
                modifier = Modifier.padding(innerPadding),
                vm = vm
            )
            Tab.GIT -> MasterSettingsView(
                modifier = Modifier.padding(innerPadding),
                vm = vm
            )
        }
    }
}