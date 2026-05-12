package org.opengamestudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import org.opengamestudio.ui.theme.MyApplicationTheme

import org.opengamestudio.checklib.*


class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        VM.androidContext = this

        SwiftInterface.initialize(this)
        SwiftInterface.registerCallback {
            println("ИГР SwiftI.getCDC: '${SwiftInterface.getCurrentDataContext()}'")
        }
        println("ИГР01")
        SwiftInterface.sendAny("url", "http://ya.ru")
        println("ИГР02")
        SwiftInterface.sendAny("selectedId", 153)
        println("ИГР03")
        SwiftInterface.sendAny("didLaunch", true)
        println("ИГР04")

        BudgetComponent.setup()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BudgetView(
                        Modifier.padding(innerPadding),
                        VM
                    )
                }
            }
        }
    }
}
