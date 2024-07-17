package com.moling.nfctoolkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoChip(title: String, content: String) {
    InfoChip(title = title, content = content, onClick = { /* Ignore */ })
}

@Composable
fun InfoChip(title: String, content: String, onClick: () -> Unit) {
    FunctionChip(Icons.Filled.Info, title, content, MaterialTheme.colorScheme.inversePrimary, onClick)
}

@Composable
fun TransparentChip(
    icon: ImageVector, title: String? = null, content: String? = null,
    background: Color? = Color.Transparent, onClick: () -> Unit
) { FunctionChip(icon, title, content, background, onClick) }

@Composable
fun TransparentChip(
    icon: Painter, title: String? = null, content: String? = null,
    background: Color? = Color.Transparent, onClick: () -> Unit
) { FunctionChip(icon, title, content, background, onClick) }

@Composable
fun FunctionChip(
    icon: ImageVector, title: String? = null, content: String? = null,
    background: Color? = MaterialTheme.colorScheme.surfaceBright, onClick: () -> Unit
) { FunctionChip(imageVector = icon, title = title, content = content, background = background, onClick = onClick) }

@Composable
fun FunctionChip(
    icon: Painter, title: String? = null, content: String? = null,
    background: Color? = MaterialTheme.colorScheme.surfaceBright, onClick: () -> Unit
) { FunctionChip(painter = icon, title = title, content = content, background = background, onClick = onClick) }

@Composable
fun FunctionChip(
    imageVector: ImageVector? = null, painter: Painter? = null, title: String? = null,
    content: String? = null, background: Color? = MaterialTheme.colorScheme.surfaceBright, onClick: () -> Unit
) {
    background?.let {
        Modifier .fillMaxWidth() .background( it, shape = RoundedCornerShape(size = 8.dp) )
    }?.let {
        AssistChip(
            onClick = onClick, modifier = it, border = null,
            label = {
                Row(Modifier.padding(horizontal = 5.dp, vertical = 10.dp)) {
                    if (imageVector != null)
                        Icon( imageVector, contentDescription = "", Modifier.size(AssistChipDefaults.IconSize * 2) )
                    else if (painter != null)
                        Icon( painter, contentDescription = "", Modifier.size(AssistChipDefaults.IconSize * 2) )
                    Column(Modifier.padding(horizontal = 10.dp)) {
                        if (title != null) Text( text = title, fontSize = 20.sp, modifier = Modifier.padding(vertical = 5.dp) )
                        if (content != null) Text(content)
                    }
                }
            }
        )
    }
}