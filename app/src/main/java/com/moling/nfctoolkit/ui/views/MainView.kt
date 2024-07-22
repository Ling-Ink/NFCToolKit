package com.moling.nfctoolkit.ui.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moling.nfctoolkit.R
import com.moling.nfctoolkit.isNFCEnabled
import com.moling.nfctoolkit.isNFCSupported
import com.moling.nfctoolkit.mMainActivityBackPressedCallback
import com.moling.nfctoolkit.ui.FunctionChip
import com.moling.nfctoolkit.ui.InfoChip
import com.moling.nfctoolkit.ui.TransparentChip

var isNFCScanCollapse by mutableStateOf(true)
var isCardsCollapse by mutableStateOf(true)
var isKeysCollapse by mutableStateOf(true)

@Composable
fun MainView(modifier: Modifier = Modifier) {

    val resNfc: Painter = painterResource(id = R.drawable.baseline_nfc_24)
    val resStyle: Painter = painterResource(id = R.drawable.baseline_style_24)
    val resKey: Painter = painterResource(id = R.drawable.baseline_key_24)
    val resHelpCenter: Painter = painterResource(id = R.drawable.baseline_help_center_24)

    val ctx: Context = LocalContext.current
    val urlHelp: String = stringResource(id = R.string.url_help)

    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 50.dp)) {
        mMainActivityBackPressedCallback.isEnabled = !isNFCScanCollapse || !isCardsCollapse || !isKeysCollapse
        Text(text = stringResource(id = R.string.app_name), fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(all = 30.dp))
        if (!(!isCardsCollapse || !isKeysCollapse)) {
            if (!isNFCSupported) InfoChip(title = stringResource(id = R.string.info_nfc_unsupported), content = stringResource(id = R.string.info_nfc_rw_disabled))
            else if (!isNFCEnabled) InfoChip(title = stringResource(id = R.string.info_nfc_disabled), content = stringResource(id = R.string.info_nfc_rw_disabled))
            else FunctionChip(icon = resNfc, title = stringResource(id = R.string.chip_scan)) { isNFCScanCollapse = !isNFCScanCollapse }
        }
        if (!isNFCScanCollapse)
            NfcScanView()
        if (!(!isNFCScanCollapse || !isKeysCollapse))
            FunctionChip(icon = resStyle, title = stringResource(id = R.string.chip_cards)) { isCardsCollapse = !isCardsCollapse }
        if (!isCardsCollapse)
            CardDumpsView()
        if (!(!isNFCScanCollapse || !isCardsCollapse))
            FunctionChip(icon = resKey, title = stringResource(id = R.string.chip_keys)) { isKeysCollapse = !isKeysCollapse }
        if (!isKeysCollapse)
            KeysView()
        if (!(!isNFCScanCollapse || !isCardsCollapse || !isKeysCollapse)) {
             TransparentChip(icon = resHelpCenter, title = stringResource(id = R.string.chip_help)) {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlHelp)))
             }
            TransparentChip(icon = Icons.Filled.Info, title = stringResource(id = R.string.chip_about)) {
                // val intent = Intent(ctx, EditorActivity::class.java)
                // intent.putExtra("type", "Key")
                // ctx.startActivity(intent)
            }
        }
    }
}
