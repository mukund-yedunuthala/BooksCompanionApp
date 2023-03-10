package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomURLDialog(
    openLibsDialog: MutableState<Boolean>,
    source: String,
    uriHandler: UriHandler
) {
    AlertDialog(
        onDismissRequest = {
            openLibsDialog.value = false
        },
        modifier = Modifier.wrapContentWidth(),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            Column {
                Text(
                    text = "Opening $source",
                    modifier = Modifier.padding(20.dp)
                )
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(
                        onClick = { openLibsDialog.value = false },
                    ) {
                        Text("Dismiss".uppercase(), Modifier.padding(10.dp))
                    }
                    TextButton(
                        onClick = {
                            openLibsDialog.value = false
                            uriHandler.openUri(source)
                        },
                    ) {
                        Text("Confirm".uppercase(), Modifier.padding(10.dp))
                    }
                }
            }
        }
    }
}