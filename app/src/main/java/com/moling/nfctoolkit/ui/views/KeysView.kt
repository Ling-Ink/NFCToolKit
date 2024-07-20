package com.moling.nfctoolkit.ui.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.moling.nfctoolkit.ui.ItemChip
import com.moling.nfctoolkit.ui.TransparentChip

@Composable
fun KeysView() {
    val ctx: Context = LocalContext.current
    
    Column(modifier = Modifier.padding(all = 10.dp)) {
        LazyColumn {
            item {
                ItemChip(icon = Icons.Filled.Add, content = "New key file") {

                }
            }
            item {
                ItemChip(content = "std.keys") {

                }
            }
            item {
                ItemChip(content = "std_extend.keys") {

                }
            }
        }
    }
}
