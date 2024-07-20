package com.moling.nfctoolkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ItemChip
 */
@Composable
fun ItemChip(content: String, onClick: () -> Unit)
    { TransparentChip(content = content, iconSize = 20.dp, padding = 0.dp, onClick = onClick) }
@Composable
fun ItemChip(icon: ImageVector, content: String, onClick: () -> Unit)
    { TransparentChip(icon = icon, iconSize = 20.dp, content = content, padding = 0.dp, onClick = onClick) }
@Composable
fun ItemChip(icon: Painter, content: String, onClick: () -> Unit)
    { TransparentChip(icon = icon, iconSize = 20.dp, content = content, padding = 0.dp, onClick = onClick) }

/**
 * InfoChip
 */
@Composable
fun InfoChip(title: String, content: String, padding: Dp = 5.dp)
    { InfoChip(title = title, content = content, padding = padding, onClick = { /* Ignore */ }) }
@Composable
fun InfoChip(title: String, content: String, padding: Dp = 5.dp, onClick: () -> Unit)
    { FunctionChip(Icons.Filled.Info, AssistChipDefaults.IconSize * 2, title, content, MaterialTheme.colorScheme.inversePrimary, padding, onClick) } /* FunctionChip_ImageVector */

/**
 * TransparentChip
 */
@Composable
fun TransparentChip(iconSize: Dp = AssistChipDefaults.IconSize * 2, title: String? = null, content: String? = null, padding: Dp = 5.dp, onClick: () -> Unit) /* FunctionChip_NoIcon */
    { FunctionChip(null, null, iconSize, title, content, Color.Transparent, padding, onClick) } /* FunctionChip_Base */
@Composable
fun TransparentChip(icon: ImageVector, iconSize: Dp = AssistChipDefaults.IconSize * 2, title: String? = null, content: String? = null, padding: Dp = 5.dp, onClick: () -> Unit)
    { FunctionChip(icon, iconSize, title, content, Color.Transparent, padding, onClick) } /* FunctionChip_ImageVector */
@Composable
fun TransparentChip(icon: Painter, iconSize: Dp = AssistChipDefaults.IconSize * 2, title: String? = null, content: String? = null, padding: Dp = 5.dp, onClick: () -> Unit)
    { FunctionChip(icon, iconSize, title, content, Color.Transparent, padding, onClick) } /* FunctionChip_Painter */

/**
 * Extend of FunctionChip
 */
@Composable
fun FunctionChip( /* FunctionChip_ImageVector */
    icon: ImageVector? = null, iconSize: Dp = AssistChipDefaults.IconSize * 2, title: String? = null, content: String? = null,
    background: Color = MaterialTheme.colorScheme.surfaceBright, padding: Dp = 5.dp, onClick: () -> Unit
) { FunctionChip(imageVector = icon, iconSize = iconSize, title = title, content = content, background = background, padding = padding, onClick = onClick) } /* FunctionChip_Base */
@Composable
fun FunctionChip( /* FunctionChip_Painter */
    icon: Painter? = null, iconSize: Dp = AssistChipDefaults.IconSize * 2, title: String? = null, content: String? = null,
    background: Color = MaterialTheme.colorScheme.surfaceBright, padding: Dp = 5.dp, onClick: () -> Unit
) { FunctionChip(painter = icon, iconSize = iconSize, title = title, content = content, background = background, padding = padding, onClick = onClick) } /* FunctionChip_Base */

/**
 * Base of FunctionChip
 */
@Composable
private fun FunctionChip( /* FunctionChip_Base */
    imageVector: ImageVector? = null, painter: Painter? = null, iconSize: Dp = 0.dp,
    title: String? = null, content: String? = null, background: Color, padding: Dp = 0.dp, onClick: () -> Unit
) {
    Box(modifier = Modifier.padding(top = padding)) {
        AssistChip(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .background(background, shape = RoundedCornerShape(size = 8.dp)),
            border = null,
            label = {
                Row(Modifier.padding(horizontal = 5.dp, vertical = 10.dp)) {
                    if (imageVector != null)
                        Icon( imageVector, contentDescription = "", Modifier.size(iconSize) )
                    else if (painter != null)
                        Icon( painter, contentDescription = "", Modifier.size(iconSize) )
                    Column(Modifier.padding(horizontal = 10.dp)) {
                        if (title != null) Text( text = title, fontSize = 20.sp, modifier = Modifier.padding(vertical = 5.dp) )
                        if (content != null) Text(content)
                    }
                }
            }
        )
    }

}