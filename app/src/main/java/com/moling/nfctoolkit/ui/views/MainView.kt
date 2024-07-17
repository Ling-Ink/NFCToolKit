package com.moling.nfctoolkit.ui.views

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moling.nfctoolkit.R
import com.moling.nfctoolkit.isNFCEnabled
import com.moling.nfctoolkit.isNFCSupported
import com.moling.nfctoolkit.mBackPressedCallback
import com.moling.nfctoolkit.ui.FunctionChip
import com.moling.nfctoolkit.ui.InfoChip

var isNFCScanCollapse by mutableStateOf(true)
var isCardDumpsCollapse by mutableStateOf(true)
var isKeysCollapse by mutableStateOf(true)

@Composable
fun MainView(modifier: Modifier = Modifier) {

    val resNfc: Painter = painterResource(id = R.drawable.baseline_nfc_24)
    val resStyle: Painter = painterResource(id = R.drawable.baseline_style_24)
    val resKey: Painter = painterResource(id = R.drawable.baseline_key_24)
    val resHelpCenter: Painter = painterResource(id = R.drawable.baseline_help_center_24)

    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 50.dp)) {
        LazyColumn(verticalArrangement = spacedBy(5.dp, Alignment.Top)) {
            item {
                Text(text = "NFC ToolKit", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(all = 30.dp))
            }
            if (!(!isCardDumpsCollapse || !isKeysCollapse)) {
                item {
                    if (!isNFCSupported) InfoChip(title = "NFC not supported", content = "NFC r/w will be disabled")
                    else if (!isNFCEnabled) InfoChip(title = "NFC not enabled", content = "NFC r/w will be disabled")
                    else FunctionChip(icon = resNfc, title = "NFC scan") { isNFCScanCollapse = !isNFCScanCollapse }
                }
            }
            if (!isNFCScanCollapse)
                item { NfcScanView() }
            if (!(!isNFCScanCollapse || !isKeysCollapse))
                item { FunctionChip(icon = resStyle, title = "Saved card dumps") { isCardDumpsCollapse = !isCardDumpsCollapse } }
            if (!isCardDumpsCollapse)
                item { CardDumpsView() }
            if (!(!isNFCScanCollapse || !isCardDumpsCollapse))
                item { FunctionChip(icon = resKey, title = "Saved keys") { isKeysCollapse = !isKeysCollapse } }
            if (!isKeysCollapse)
                item { KeysView() }
            if (!(!isNFCScanCollapse || !isCardDumpsCollapse || !isKeysCollapse)) {
                item {
                    FunctionChip(icon = resHelpCenter, title = "Help", background = Color.Transparent) {

                    }
                }
                item {
                    FunctionChip(icon = Icons.Filled.Info, title = "About", background = Color.Transparent) {

                    }
                }
            }
            mBackPressedCallback.isEnabled = !isNFCScanCollapse || !isCardDumpsCollapse || !isKeysCollapse
        }
    }
}
