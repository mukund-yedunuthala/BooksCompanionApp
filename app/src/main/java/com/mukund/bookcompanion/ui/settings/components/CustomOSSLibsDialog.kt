package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.design.IBMPlexSans
import com.mukund.bookcompanion.design.JetBrainsMono
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@Composable
fun CustomURLDialog(
    source: String,
    onDismiss: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = bookColors.paper,
        shape = RectangleShape,
        title = {
            Text(
                text = stringResource(R.string.oss_dialog_title),
                style = AppType.headingSerif.copy(fontStyle = FontStyle.Italic),
                color = bookColors.ink,
            )
        },
        text = {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(
                        fontFamily = IBMPlexSans,
                        fontSize = 13.sp,
                        color = bookColors.inkSoft,
                    )) { append(stringResource(R.string.oss_dialog_intro)) }
                    withLink(
                        LinkAnnotation.Url(
                            url = source,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    fontFamily = JetBrainsMono,
                                    fontSize = 11.sp,
                                    color = bookColors.terracotta,
                                    textDecoration = TextDecoration.Underline,
                                )
                            )
                        ) {
                            uriHandler.openUri((it as LinkAnnotation.Url).url)
                        }
                    ) { append(source) }
                },
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    uriHandler.openUri(source)
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.oss_dialog_open),
                    style = AppType.labelTinyMono,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = AppType.labelTinyMono,
                )
            }
        },
    )
}
