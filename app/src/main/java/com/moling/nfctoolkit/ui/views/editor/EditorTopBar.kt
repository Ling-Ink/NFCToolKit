package com.moling.nfctoolkit.ui.views.editor

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.moling.nfctoolkit.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditorTopBar(fileName: String) {

    val resSave: Painter = painterResource(id = R.drawable.baseline_save_24)
    val resSaveAs: Painter = painterResource(id = R.drawable.baseline_save_as_24)

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = fileName)
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    painter = resSave,
                    contentDescription = "Localized description"
                )
            }
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    painter = resSaveAs,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}