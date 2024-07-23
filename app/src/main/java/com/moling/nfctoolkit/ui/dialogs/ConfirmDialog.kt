package com.moling.nfctoolkit.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmDialog(title: String, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = title, style = MaterialTheme.typography.headlineMedium) },
        text = { Text(text = text, fontSize = 16.sp) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Yes", style = MaterialTheme.typography.bodySmall)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}