package com.moling.nfctoolkit.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun EditorUnsavedDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {  },
        title = {
            Text(text = "File unsaved", style = MaterialTheme.typography.headlineMedium)
        },
        text = {
            Text(text = "Force quit? If you click Yes, you will lost your work", fontSize = 16.sp)
        },
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