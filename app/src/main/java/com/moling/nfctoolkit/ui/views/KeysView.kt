package com.moling.nfctoolkit.ui.views

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.moling.nfctoolkit.MainActivity
import com.moling.nfctoolkit.R
import com.moling.nfctoolkit.ui.ItemChip
import java.io.File


@Composable
fun KeysView(act: MainActivity) {
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
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                    }
                    act.keyImportLauncher.launch(intent)
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

fun importKeyFile(act: MainActivity, path: String) {
    val keyFile = File(path)
    if (keyFile.extension != "keys") {
        Log.d(act.LOG_TAG, "Selected file not *.keys, is ${keyFile.extension}")
        return
    }
    Log.d(act.LOG_TAG, "Importing: $path")
}
