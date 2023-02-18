package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

val BUTTON_HEIGHT = 70.dp
val ICON_SIZE = 25.dp
@Composable
fun CustomEntryButton (
    onClick: () -> Unit = {},
    imageVector : ImageVector,
    contentDescription : String,
    leadText : String,
    subText : String?
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(BUTTON_HEIGHT),
        shape = RectangleShape,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .size(ICON_SIZE)
                .align(Alignment.CenterVertically)
        )
        Column(
            Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth()
        ) {
            Text(
                text = leadText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(20.dp, bottom = 3.dp),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Left,
            )
            if (subText != null) {
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(20.dp, top = 2.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                )
            }
        }
    }
}
@Composable
fun CustomEntryButton (
    onClick: () -> Unit = {},
    painter : Painter,
    contentDescription : String,
    leadText : String,
    subText : String? = null
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(BUTTON_HEIGHT),
        shape = RectangleShape,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .size(ICON_SIZE)
                .align(Alignment.CenterVertically)
        )
        Column(
            Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth()
        ) {
            Text(
                text = leadText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(20.dp, bottom = 2.dp),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Left
            )
            if (subText != null) {
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(20.dp, top = 3.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}
