package com.moling.nfctoolkit

import android.app.PendingIntent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

private val LOG_TAG = "NFCToolKit"

// NFC status
private var isNFCSupported by mutableStateOf(false)
private var isNFCEnabled by mutableStateOf(false)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if NFC is supported and enabled
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        isNFCSupported = nfcAdapter != null
        Log.d(LOG_TAG, "NFC supported: %s".format(isNFCSupported.toString()))
        isNFCEnabled = nfcAdapter?.isEnabled == true
        Log.d(LOG_TAG, "NFC enabled: %s".format(isNFCEnabled.toString()))

        if (isNFCSupported) {
            Thread{
                while (true) {
                    isNFCEnabled = nfcAdapter?.isEnabled == true
                    sleep(500)
                }
            }.start()
        }


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
    Column(
        modifier = modifier.padding(horizontal = 40.dp, vertical = 50.dp)
    ) {
        LazyColumn(verticalArrangement = spacedBy(5.dp, Alignment.Top)) {
            item {
                Text(text = "NFC ToolKit", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(all = 30.dp))
            }
            item {
                if (!isNFCSupported) {
                    InfoChip(title = "NFC not supported", content = "NFC r/w will be disabled")
                }
                else if (!isNFCEnabled) {
                    InfoChip(title = "NFC not enabled", content = "NFC r/w will be disabled")
                }
                else {
                    FunctionChip(icon = painterResource(id = R.drawable.baseline_nfc_24), title = "NFC scan") {

                    }
                }
            }
            item {
                FunctionChip(icon = painterResource(id = R.drawable.baseline_style_24), title = "Saved card dumps") {

                }
            }
            item {
                FunctionChip(icon = painterResource(id = R.drawable.baseline_key_24), title = "Saved keys") {

                }
            }
        }
    }
}