package com.moling.nfctoolkit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SimpleInfo(title: String, content: String) {
    Text(text = title, fontWeight = FontWeight.Bold)
    Text(text = content, modifier = Modifier.padding(start = 10.dp))
}
