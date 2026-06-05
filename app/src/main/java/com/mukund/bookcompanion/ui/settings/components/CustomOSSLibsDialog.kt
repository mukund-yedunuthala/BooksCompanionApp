package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.design.CormorantGaramond
import com.mukund.bookcompanion.design.IBMPlexSans
import com.mukund.bookcompanion.design.JetBrainsMono
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
                text = "Open link?",
                fontFamily = CormorantGaramond,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
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
                    )) { append("This will open:\n") }
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
            Box(
                modifier = Modifier
                    .background(bookColors.ink)
                    .clickable {
                        uriHandler.openUri(source)
                        onDismiss()
                    }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Open",
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    letterSpacing = 0.14.sp,
                    color = bookColors.paper,
                )
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .border(width = 0.5.dp, color = bookColors.rule)
                    .clickable { onDismiss() }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel",
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    letterSpacing = 0.14.sp,
                    color = bookColors.inkSoft,
                )
            }
        },
    )
}
