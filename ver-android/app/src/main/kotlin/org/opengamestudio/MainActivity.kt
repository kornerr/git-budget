package org.opengamestudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import org.opengamestudio.ui.theme.MyApplicationTheme

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val vm = VM
        vm.androidContext = this
        MastComponent.setup()

        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = vm.isBudgetTabSelected.value,
                                onClick = { mastSet(F.didClickBudgetTab, true) },
                                label = { Text("Budget") },
                                icon = { Icon(Icons.Default.Home, contentDescription = null) }
                            )
                            NavigationBarItem(
                                selected = vm.isSettingsTabSelected.value,
                                onClick = { mastSet(F.didClickSettingsTab, true) },
                                label = { Text("Settings") },
                                icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                            )
                        }
                    }
                ) { innerPadding ->
                    if (vm.isBudgetTabSelected.value) {
                        BudgView(
                            modifier = Modifier.padding(innerPadding),
                            vm = vm
                        )
                    }
                    if (vm.isSettingsTabSelected.value) {
                        MastSettingsView(
                            modifier = Modifier.padding(innerPadding),
                            vm = vm
                        )
                    }
                }
            }
        }
    }
}
