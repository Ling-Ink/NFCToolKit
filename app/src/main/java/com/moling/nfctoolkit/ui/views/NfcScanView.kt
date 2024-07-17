package com.moling.nfctoolkit.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moling.nfctoolkit.ui.SimpleInfo

// Tag info
var tagUID by mutableStateOf("-")
var tagTech by mutableStateOf("-")
var tagATQA by mutableStateOf("-")
var tagSAK by mutableStateOf("-")
var tagMifareSize by mutableStateOf("-")
var tagMifareSectorCount by mutableStateOf("-")
var tagMifareBlockCount by mutableStateOf("-")

@Composable
fun NfcScanView() {
    var tabState by remember { mutableIntStateOf(0) }
    val titles = listOf("Info", "Read", "Write")
    Column(modifier = Modifier.padding(all = 10.dp)) {
        TabRow(selectedTabIndex = tabState) {
            titles.forEachIndexed { index, title ->
                Tab(selected = tabState == index, onClick = { tabState = index }) {
                    Text(text = title, modifier = Modifier
                        .height(40.dp)
                        .wrapContentSize(Alignment.Center))
                }
            }
        }
        when (tabState) {
            0 -> InfoTab()
            1 -> ReadTab()
            2 -> WriteTab()
        }
    }
}

@Composable
fun InfoTab() {
    SimpleInfo(title = "UID", content = tagUID)
    SimpleInfo(title = "Tech", content = tagTech)
    SimpleInfo(title = "ATQA", content = tagATQA)
    SimpleInfo(title = "SAK", content = tagSAK)
    SimpleInfo(title = "Mifare memory size", content = tagMifareSize)
    SimpleInfo(title = "Mifare sector count", content = tagMifareSectorCount)
    SimpleInfo(title = "Mifare block count", content = tagMifareBlockCount)
}

@Composable
fun ReadTab() {
    Text(text = "NfcScanView - Read")
}

@Composable
fun WriteTab() {
    Text(text = "NfcScanView - Write")
}
