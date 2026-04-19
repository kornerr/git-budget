package org.opengamestudio

import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Paste
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BudgetScreen(
    modifier: Modifier = Modifier,
    vm: VM
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        content = { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                tonalElevation = 4.dp
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BudgetInputField(
                        title = vm.spentPlaceholder.value,
                        value = vm.spent.value,
                        onValueChange = { budgetSet(F.inputSpent, it) },
                        pasteAction = { budgetSet(F.didClickPasteSpent, true) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    BudgetInputField(
                        title = vm.morningBalancePlaceholder.value,
                        value = vm.morningBalance.value,
                        onValueChange = { budgetSet(F.inputMorningBalance, it) },
                        pasteAction = { budgetSet(F.didClickPasteMorningBalance, true) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = vm.result.value,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    FilledTonalButton(
                        onClick = { budgetSet(F.didClickCopy, true) },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(vm.resultCopyTitle.value)
                    }
                }
            }
        }
    )
}

@Composable
private fun BudgetInputField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    pasteAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            placeholder = { Text(title) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .fillMaxWidth()
        )
        
        IconButton(
            onClick = pasteAction,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Paste,
                contentDescription = "Вставить",
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
