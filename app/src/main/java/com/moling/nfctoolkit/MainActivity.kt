package com.moling.nfctoolkit

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.moling.nfctoolkit.ui.dialogs.ConfirmDialog
import com.moling.nfctoolkit.ui.theme.NFCToolKitTheme
import com.moling.nfctoolkit.ui.views.MainView
import com.moling.nfctoolkit.ui.views.importKeyFile
import com.moling.nfctoolkit.ui.views.isCardsCollapse
import com.moling.nfctoolkit.ui.views.isKeysCollapse
import com.moling.nfctoolkit.ui.views.isNFCScanCollapse
import com.moling.nfctoolkit.ui.views.tagATQA
import com.moling.nfctoolkit.ui.views.tagMifareBlockCount
import com.moling.nfctoolkit.ui.views.tagMifareSectorCount
import com.moling.nfctoolkit.ui.views.tagMifareSize
import com.moling.nfctoolkit.ui.views.tagSAK
import com.moling.nfctoolkit.ui.views.tagTech
import com.moling.nfctoolkit.ui.views.tagUID
import com.moling.nfctoolkit.utils.FileUtils
import com.moling.nfctoolkit.utils.getATQA
import com.moling.nfctoolkit.utils.getMifareBlockCount
import com.moling.nfctoolkit.utils.getMifareSectorCount
import com.moling.nfctoolkit.utils.getMifareSize
import com.moling.nfctoolkit.utils.getSAK
import com.moling.nfctoolkit.utils.getTag
import com.moling.nfctoolkit.utils.getTech
import com.moling.nfctoolkit.utils.getUID
import java.lang.Thread.sleep

// NFC adapter for checking NFC state in the device
private var nfcAdapter : NfcAdapter? = null

// Pending intent for NFC intent foreground dispatch.
// Used to read all NDEF tags while the app is running in the foreground.
private var nfcPendingIntent: PendingIntent? = null

// Back pressed dispatcher
val mMainActivityBackPressedCallback: OnBackPressedCallback by lazy  {
    object : OnBackPressedCallback(false) { // enabled Flag
        override fun handleOnBackPressed() {
            isNFCScanCollapse = true
            isCardsCollapse = true
            isKeysCollapse = true
        }
    }
}

// NFC status
var isNFCSupported by mutableStateOf(false)
var isNFCEnabled by mutableStateOf(false)

// Local file storage
var appFilesPath: String? = null

// Application permission about
var hidePermissionRequest by mutableStateOf(false)
var isPermissionGranted by mutableStateOf(false)

class MainActivity : ComponentActivity() {

    val LOG_TAG = "NFCToolKit_Main"

    val keyImportLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            importKeyFile(this, FileUtils.getFile(baseContext, result.data!!.data)!!.absolutePath)
        }
    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null) enableForegroundDispatch(nfcAdapter!!)
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action != NfcAdapter.ACTION_TECH_DISCOVERED) return
        Log.d(LOG_TAG, "=====ACTION_TECH_DISCOVERED=====")

        val tag: Tag = intent.getTag()
        getTagInfo(tag)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (nfcAdapter != null && hasFocus && !isNFCEnabled) enableForegroundDispatch(nfcAdapter!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appInit()

        setContent {
            NFCToolKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(this, modifier = Modifier.padding(innerPadding))
                    isPermissionGranted = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    Log.d(LOG_TAG, "Storage permission: $isPermissionGranted")
                    if (!isPermissionGranted) {
                        if (!hidePermissionRequest) {
                            ConfirmDialog(
                                title = stringResource(id = R.string.dialog_perm_request),
                                text = stringResource(id = R.string.dialog_perm_storage),
                                onConfirm = {
                                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 222)
                                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 222)
                                    hidePermissionRequest = true
                                },
                                onDismiss = { hidePermissionRequest = true }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun appInit() {
        // Check if NFC is supported and enabled
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        isNFCSupported = nfcAdapter != null
        isNFCEnabled = nfcAdapter?.isEnabled == true

        appFilesPath = filesDir.absolutePath
        Log.d(LOG_TAG, "App file path: ${filesDir.absolutePath}")

        // NFC status check
        if (isNFCSupported) {
            Thread{
                while (true) {
                    isNFCEnabled = nfcAdapter?.isEnabled == true
                    if (!isNFCEnabled) isNFCScanCollapse = true
                    sleep(500)
                }
            }.start()
        }

        // Storage permission check
        if (!isPermissionGranted) {
            Thread{
                var run = true
                while (run) {
                    isPermissionGranted = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    if (isPermissionGranted) run = false
                    sleep(500)
                }
            }.start()
        }

        this.onBackPressedDispatcher.addCallback(this, mMainActivityBackPressedCallback)

        nfcPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE)



    }

    private fun enableForegroundDispatch(nfcAdapter: NfcAdapter) {
        nfcAdapter.enableForegroundDispatch(
            this, nfcPendingIntent, null,
            arrayOf(
                arrayOf(NfcA::class.java.name),
                arrayOf(NfcB::class.java.name),
            )
        )
    }

    private fun getTagInfo(tag: Tag) {
        tagUID = tag.getUID()
        Log.d(LOG_TAG, "UID: $tagUID")
        tagTech = tag.getTech().joinToString()
        Log.d(LOG_TAG, "Tech: $tagTech")
        tagATQA = tag.getATQA()
        Log.d(LOG_TAG, "ATQA: $tagATQA")
        tagSAK = tag.getSAK()
        Log.d(LOG_TAG, "SAK: $tagSAK")
        tagMifareSize = tag.getMifareSize()
        Log.d(LOG_TAG, "MifareSize: $tagMifareSize")
        tagMifareSectorCount = tag.getMifareSectorCount()
        Log.d(LOG_TAG, "MifareSectorCount: $tagMifareSectorCount")
        tagMifareBlockCount = tag.getMifareBlockCount()
        Log.d(LOG_TAG, "MifareBlockCount: $tagMifareBlockCount")
    }
}
