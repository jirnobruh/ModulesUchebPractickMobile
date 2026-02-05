package com.example.collegeschedule.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GroupChip(
    group: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        FilledTonalButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
        ) {
            Text(group)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
        ) {
            Text(group)
        }
    }
}