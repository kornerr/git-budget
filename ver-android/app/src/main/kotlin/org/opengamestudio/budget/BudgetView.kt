package org.opengamestudio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun BudgetView(
    modifier: Modifier = Modifier,
    vm: VM,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Row {
            TextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.weight(2f),
                onValueChange = { budgetSet(F.inputSpent, it) },
                placeholder = { Text(vm.spentPlaceholder.value) },
                value = vm.spent.value,
            )
            Spacer(Modifier.width(8.dp))
            ElevatedButton(
                modifier = Modifier.weight(1f),
                onClick = { budgetSet(F.didClickPasteSpent, true) },
            ) {
                Text(vm.spentPasteTitle.value)
            }
        }
        Row {
            TextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.padding(top = 12.dp).weight(2f),
                onValueChange = {
                    vm.inputMorningBalance.value = it 
                    budgetCtrl().set("inputMorningBalance", it)
                },
                placeholder = { Text(vm.inputMorningBalancePlaceholder.value) },
                value = vm.inputMorningBalance.value,
            )
            ElevatedButton(
                modifier = Modifier.weight(1f),
                onClick = { budgetCtrl().set("didClickPasteMorningBalance", true) },
            ) {
                Text(vm.inputMorningBalancePasteTitle.value)
            }
        }
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
