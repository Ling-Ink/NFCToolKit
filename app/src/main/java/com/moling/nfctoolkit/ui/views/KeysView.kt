package com.moling.nfctoolkit.ui.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moling.nfctoolkit.EditorActivity
import com.moling.nfctoolkit.R
import com.moling.nfctoolkit.ui.ItemChip

@Composable
fun KeysView() {
    val resImport: Painter = painterResource(id = R.drawable.baseline_download_24)

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
            item {
                ItemChip(icon = resImport, content = stringResource(id = R.string.keyfile_import)) {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"

                        // Optionally, specify a URI for the file that should appear in the
                        // system file picker when it loads.
                        putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("/storage/emulated/0/"))
                    }
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
