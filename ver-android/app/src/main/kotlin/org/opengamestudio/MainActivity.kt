package org.opengamestudio

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.opengamestudio.ui.theme.MyApplicationTheme

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        VM.androidContext = this
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Budget(
                        vm = VM,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

//<!-- Budget -->

@Composable
fun Budget(
    modifier: Modifier = Modifier,
    vm: VM,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            TextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = {
                    Text(
                        style = MaterialTheme.typography.labelLarge,
                        text = vm.inputSpentLabel.value,
                    )
                },
                modifier = Modifier.weight(2f),
                onValueChange = {
                    vm.inputSpent.value = it 
                    budgetCtrl().set("inputSpent", it)
                },
                placeholder = { Text(vm.inputSpentPlaceholder.value) },
                value = vm.inputSpent.value,
            )
            ElevatedButton(
                modifier = Modifier.weight(1f),
                onClick = { budgetCtrl().set("didClickPaste", true) },
            ) {
                Text(vm.inputSpentPasteTitle.value)
            }
        }
        TextField(
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = {
                Text(
                    style = MaterialTheme.typography.labelLarge,
                    text = vm.inputMorningBalanceLabel.value,
                )
            },
            modifier = Modifier.padding(top = 12.dp),
            onValueChange = {
                vm.inputMorningBalance.value = it 
                budgetCtrl().set("inputMorningBalance", it)
            },
            placeholder = { Text(vm.inputMorningBalancePlaceholder.value) },
            value = vm.inputMorningBalance.value,
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = vm.result.value,
        )
        ElevatedButton(
            modifier = Modifier.padding(top = 8.dp),
            onClick = { budgetCtrl().set("didClickCopy", true) },
        ) {
            Text(vm.resultCopyTitle.value)
        }
    }
}
