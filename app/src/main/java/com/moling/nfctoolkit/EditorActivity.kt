package com.moling.nfctoolkit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.moling.nfctoolkit.ui.dialogs.EditorUnsavedDialog
import com.moling.nfctoolkit.ui.theme.NFCToolKitTheme
import com.moling.nfctoolkit.ui.views.editor.DumpView
import com.moling.nfctoolkit.ui.views.editor.EditorTopBar
import com.moling.nfctoolkit.ui.views.editor.KeyView

private const val LOG_TAG = "NFCToolKit_Editor"

private var showUnsavedDialog by mutableStateOf(false)

// Back pressed dispatcher
val mEditorActivityBackPressedCallback: OnBackPressedCallback by lazy  {
    object : OnBackPressedCallback(false) { // enabled Flag
        override fun handleOnBackPressed() {
            showUnsavedDialog = true
        }
    }
}

class EditorActivity : ComponentActivity() {

    private lateinit var type: String
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        editorInit()

        setContent {
            NFCToolKitTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        EditorTopBar(fileName = path)
                    },
                    content = { innerPadding ->
                        if (type == "dump")
                            DumpView(modifier = Modifier.padding(innerPadding))
                        else if (type == "key")
                            KeyView(modifier = Modifier.padding(innerPadding), fileName = path)
                    }
                )
                if (showUnsavedDialog)
                    EditorUnsavedDialog(
                        onConfirm = {
                            showUnsavedDialog = false;
                            mEditorActivityBackPressedCallback.isEnabled = false;
                            finish()
                        },
                        onDismiss = { showUnsavedDialog = false }
                    )
            }
        }
    }

    private fun editorInit() {
        type = intent.getStringExtra("type")!!
        path = intent.getStringExtra("path")!!
        Log.d(LOG_TAG, "Editor init: $type | $path")

        this.onBackPressedDispatcher.addCallback(this, mEditorActivityBackPressedCallback)
    }
}