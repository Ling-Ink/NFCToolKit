package com.moling.nfctoolkit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.moling.nfctoolkit.ui.theme.NFCToolKitTheme
import com.moling.nfctoolkit.ui.views.editor.KeyView

private const val LOG_TAG = "NFCToolKit_Editor"

class EditorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        editorInit()

        setContent {
            NFCToolKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KeyView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun editorInit() {
        val type: String = intent.getStringExtra("type")!!
        val path: String = intent.getStringExtra("path")!!
        Log.d(LOG_TAG, "Editor init: $type | $path")
    }
}