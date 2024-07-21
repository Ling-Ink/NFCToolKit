package com.moling.nfctoolkit.ui.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moling.nfctoolkit.EditorActivity
import com.moling.nfctoolkit.R
import com.moling.nfctoolkit.ui.ItemChip

@Composable
fun KeysView() {
    val ctx: Context = LocalContext.current
    val keys = mutableListOf("")
    keys.removeFirst()
    // STD key files
    keys.add("std.keys")
    keys.add("std_extend.keys")
    
    Column(modifier = Modifier.padding(all = 10.dp)) {
        LazyColumn {
            item {
                ItemChip(icon = Icons.Filled.Add, content = stringResource(id = R.string.keyfile_new)) {
                    val intent = Intent(ctx, EditorActivity::class.java)
                    intent.putExtra("type", "key")
                    intent.putExtra("path", "new")
                    ctx.startActivity(intent)
                }
            }
            items(keys) {
                ItemChip(content = it) {
                    val intent = Intent(ctx, EditorActivity::class.java)
                    intent.putExtra("type", "key")
                    intent.putExtra("path", it)
                    ctx.startActivity(intent)
                }
            }
        }
    }
}
