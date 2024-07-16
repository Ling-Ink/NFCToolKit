package com.moling.nfctoolkit

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moling.nfctoolkit.ui.FunctionChip
import com.moling.nfctoolkit.ui.InfoChip
import com.moling.nfctoolkit.ui.theme.NFCToolKitTheme
import java.lang.Thread.sleep

// NFC adapter for checking NFC state in the device
private var nfcAdapter : NfcAdapter? = null

// Pending intent for NFC intent foreground dispatch.
// Used to read all NDEF tags while the app is running in the foreground.
private var nfcPendingIntent: PendingIntent? = null

private const val LOG_TAG = "NFCToolKit"

// NFC status
private var isNFCSupported by mutableStateOf(false)
private var isNFCEnabled by mutableStateOf(false)

class MainActivity : ComponentActivity() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action != NfcAdapter.ACTION_TECH_DISCOVERED) return
        Log.d(LOG_TAG, "ACTION_TECH_DISCOVERED")

        val tag: Tag
        try {
            tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)!!
            } else {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
            }
        } catch (ex: RuntimeException) {
            Log.e(LOG_TAG, ex.toString())
            return
        }
        val techList: Array<out String> = tag.techList
        for (tech in techList) {
            Log.d(LOG_TAG, tech)
        }
    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null) {
            nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, arrayOf(arrayOf(NfcA::class.java.name)))
        }
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) {
            nfcAdapter?.disableForegroundDispatch(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if NFC is supported and enabled
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        isNFCSupported = nfcAdapter != null
        isNFCEnabled = nfcAdapter?.isEnabled == true

        if (isNFCSupported) {
            Thread{
                while (true) {
                    isNFCEnabled = nfcAdapter?.isEnabled == true
                    sleep(500)
                }
            }.start()
        }

        nfcPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE)

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
}

@Composable
fun MainView(modifier: Modifier = Modifier) {
    var isNFCScanCollapse by remember { mutableStateOf(true) }
    var isCardDumpsCollapse by remember { mutableStateOf(true) }
    var isKeysCollapse by remember { mutableStateOf(true) }
    Column(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 50.dp)
    ) {
        LazyColumn(verticalArrangement = spacedBy(5.dp, Alignment.Top)) {
            item {
                Text(text = "NFC ToolKit", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(all = 30.dp))
            }
            if (!(!isCardDumpsCollapse || !isKeysCollapse)) {
                item {
                    if (!isNFCSupported) {
                        InfoChip(title = "NFC not supported", content = "NFC r/w will be disabled")
                    }
                    else if (!isNFCEnabled) {
                        InfoChip(title = "NFC not enabled", content = "NFC r/w will be disabled")
                    }
                    else {
                        FunctionChip(icon = painterResource(id = R.drawable.baseline_nfc_24), title = "NFC scan") {
                            isNFCScanCollapse = !isNFCScanCollapse
                        }
                    }
                }
            }
            if (!isNFCScanCollapse) {
                item {
                    NFCScanView()
                }
            }
            if (!(!isNFCScanCollapse || !isKeysCollapse)) {
                item {
                    FunctionChip(icon = painterResource(id = R.drawable.baseline_style_24), title = "Saved card dumps") {
                        isCardDumpsCollapse = !isCardDumpsCollapse
                    }
                }
            }
            if (!isCardDumpsCollapse) {
                item {
                    CardDumpsView()
                }
            }
            if (!(!isNFCScanCollapse || !isCardDumpsCollapse)) {
                item {
                    FunctionChip(icon = painterResource(id = R.drawable.baseline_key_24), title = "Saved keys") {
                        isKeysCollapse = !isKeysCollapse
                    }
                }
            }
            if (!isKeysCollapse) {
                item {
                    KeysView()
                }
            }
            if (!(!isNFCScanCollapse || !isCardDumpsCollapse || !isKeysCollapse)) {
                item {
                    FunctionChip(icon = painterResource(id = R.drawable.baseline_help_center_24), title = "Help", background = Color.Transparent) {

                    }
                }
                item {
                    FunctionChip(icon = Icons.Filled.Info, title = "About", background = Color.Transparent) {

                    }
                }
            }
        }
    }
}

@Composable
fun NFCScanView() {
    Column(modifier = Modifier.padding(all = 10.dp)) {
        Text(text = "NfcScanView")
    }
}

@Composable
fun CardDumpsView() {
    Column(modifier = Modifier.padding(all = 10.dp)) {
        Text(text = "CardDumpsView")
    }
}

@Composable
fun KeysView() {
    Column(modifier = Modifier.padding(all = 10.dp)) {
        Text(text = "KeysView")
    }
}