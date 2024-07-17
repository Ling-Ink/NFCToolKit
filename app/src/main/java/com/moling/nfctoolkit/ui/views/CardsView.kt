package com.moling.nfctoolkit.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CardDumpsView() {
    Column(modifier = Modifier.padding(all = 10.dp)) {
        Text(text = "CardsView")
    }
}
