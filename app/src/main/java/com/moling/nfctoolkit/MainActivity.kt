package com.moling.nfctoolkit

import android.app.PendingIntent
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.moling.nfctoolkit.ui.theme.NFCToolKitTheme
import com.moling.nfctoolkit.ui.views.MainView
import com.moling.nfctoolkit.ui.views.isCardDumpsCollapse
import com.moling.nfctoolkit.ui.views.isKeysCollapse
import com.moling.nfctoolkit.ui.views.isNFCScanCollapse
import com.moling.nfctoolkit.ui.views.tagATQA
import com.moling.nfctoolkit.ui.views.tagMifareBlockCount
import com.moling.nfctoolkit.ui.views.tagMifareSectorCount
import com.moling.nfctoolkit.ui.views.tagMifareSize
import com.moling.nfctoolkit.ui.views.tagSAK
import com.moling.nfctoolkit.ui.views.tagTech
import com.moling.nfctoolkit.ui.views.tagUID
import java.lang.Thread.sleep

private const val LOG_TAG = "NFCToolKit"

// NFC adapter for checking NFC state in the device
private var nfcAdapter : NfcAdapter? = null

// Pending intent for NFC intent foreground dispatch.
// Used to read all NDEF tags while the app is running in the foreground.
private var nfcPendingIntent: PendingIntent? = null

// Back pressed dispatcher
val mBackPressedCallback: OnBackPressedCallback by lazy  {
    object : OnBackPressedCallback(false) { // enabled Flag
        override fun handleOnBackPressed() {
            isNFCScanCollapse = true
            isCardDumpsCollapse = true
            isKeysCollapse = true
        }
    }
}

// NFC status
var isNFCSupported by mutableStateOf(false)
var isNFCEnabled by mutableStateOf(false)

class MainActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null)
            nfcAdapter?.enableForegroundDispatch(
                this, nfcPendingIntent, null,
                arrayOf(
                    arrayOf(NfcA::class.java.name),
                    arrayOf(NfcB::class.java.name),
                )
            )
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appInit()

        setContent {
            NFCToolKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun appInit() {
        // Check if NFC is supported and enabled
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        isNFCSupported = nfcAdapter != null
        isNFCEnabled = nfcAdapter?.isEnabled == true

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

        nfcPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE)

        this.onBackPressedDispatcher.addCallback(this, mBackPressedCallback)
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
