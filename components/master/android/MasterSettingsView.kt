package org.opengamestudio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MasterSettingsView(
    modifier: Modifier = Modifier,
    vm: VM,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(vm.logs) { item ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = item,
            )
        }
    }
}
