package com.moling.nfctoolkit.ui.views.editor

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.moling.nfctoolkit.mEditorActivityBackPressedCallback
import com.moling.nfctoolkit.utils.getKeyFile

@Composable
fun KeyView(modifier: Modifier = Modifier, fileName: String) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent
    )

    var keyContents by remember { mutableStateOf("") }
    val ctx: Context = LocalContext.current

    if (fileName == "std.keys")
        keyContents = ctx.assets.getKeyFile("std.keys")
    else if (fileName == "std_extend.keys")
        keyContents = ctx.assets.getKeyFile("std_extend.keys")

    Column(modifier = modifier.padding(all = 10.dp)) {
        TextField(
            value = keyContents,
            onValueChange = {
                keyContents = it
                mEditorActivityBackPressedCallback.isEnabled = true
            },
            label = {  },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )
    }
}  