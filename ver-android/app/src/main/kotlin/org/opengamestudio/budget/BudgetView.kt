package org.opengamestudio

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.*
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
            .padding(top = 8.dp)
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
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = { budgetSet(F.didClickPasteSpent, true) },
            ) {
                Text(vm.spentPasteTitle.value)
            }
        }
        Spacer(Modifier.height(8.dp))
        Row {
            TextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.weight(2f),
                onValueChange = { budgetSet(F.inputMorningBalance, it) },
                placeholder = { Text(vm.morningBalancePlaceholder.value) },
                value = vm.morningBalance.value,
            )
            Spacer(Modifier.width(8.dp))
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = { budgetSet(F.didClickPasteMorningBalance, true) },
            ) {
                Text(vm.morningBalancePasteTitle.value)
            }
        }
        Spacer(Modifier.height(8.dp))
        Column(modifier = Modifier
            .border(
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp),
                width = 1.dp
            )
            .padding(8.dp)
            .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = vm.result.value,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(
                    onClick = { budgetSet(F.didClickCopy, true) },
                ) {
                    Text(vm.resultCopyTitle.value)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            //horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledTonalButton(
                onClick = { budgetSet(F.didClickCommit, true) },
            ) {
                Text(vm.commitTitle.value)
            }
            Spacer(modifier = Modifier.fillMaxWidth())
            FilledTonalButton(
                onClick = { budgetSet(F.didClickPush, true) },
            ) {
                Text(vm.pushTitle.value)
            }
        }
    }
}
