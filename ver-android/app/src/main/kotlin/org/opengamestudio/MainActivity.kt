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
        println("ИГР01")
        SwiftInterface.sendInt(200)
        println("ИГР02")
        SwiftInterface.fetchAsyncDataWithCallback { result: String ->
            println("ИГР SwiftI.fetchADWC result: '$result'")
        }
        println("ИГР03")
        SwiftInterface.sendAny("url", "http://ya.ru")
        var ctx = SwiftInterface.getCurrentDataContext()
        println("ИГР04 ctx: '$ctx'")
        SwiftInterface.sendAny("selectedId", 153)
        println("ИГР05")
        SwiftInterface.sendAny("didLaunch", true)
        println("ИГР06")

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
