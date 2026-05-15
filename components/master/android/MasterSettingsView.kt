package org.opengamestudio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MasterSettingsView(
    modifier: Modifier = Modifier,
    vm: VM,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Git",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
