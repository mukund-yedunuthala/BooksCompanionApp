package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
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
                BasicText(
                    buildAnnotatedString {
                        val link =
                            LinkAnnotation.Url(
                                source, TextLinkStyles(
                                    SpanStyle(
                                        color = Color.Blue,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            ) {
                                val url = (it as LinkAnnotation.Url).url
                                // log some metrics
                                uriHandler.openUri(url)
                            }
                        withLink(link) {append(source)}
                    }
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
