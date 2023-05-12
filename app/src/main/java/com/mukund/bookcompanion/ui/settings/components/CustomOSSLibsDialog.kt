package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

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
        text = {
            Column() {
                Text(
                    text = "Opening: ",
                    style = MaterialTheme.typography.bodyLarge
                )
                ClickableText(
                    text = AnnotatedString(
                        text = source,
                    ),
                    onClick = {
                        openLibsDialog.value = false
                        uriHandler.openUri(source)
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openLibsDialog.value = false
                    uriHandler.openUri(source)
                },
            ) {
                Text("Confirm".uppercase(), Modifier.padding(10.dp))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { openLibsDialog.value = false },
            ) {
                Text("Dismiss".uppercase(), Modifier.padding(10.dp))
            }
        }
    )
}
