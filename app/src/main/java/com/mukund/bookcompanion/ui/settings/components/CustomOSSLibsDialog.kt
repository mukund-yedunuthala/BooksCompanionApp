package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink

@Composable
fun CustomURLDialog(
    source: String,
    onDismiss: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Open link?")
        },
        text = {
            Text(
                text = buildAnnotatedString {
                    append("This will open:\n")
                    withLink(
                        LinkAnnotation.Url(
                            url = source,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                )
                            )
                        ) {
                            uriHandler.openUri((it as LinkAnnotation.Url).url)
                        }
                    ) {
                        append(source)
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            TextButton(onClick = {
                uriHandler.openUri(source)
                onDismiss()
            }) {
                Text("Open")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
