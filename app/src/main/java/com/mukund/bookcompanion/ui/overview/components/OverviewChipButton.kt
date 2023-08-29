package com.mukund.bookcompanion.ui.overview.components
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomEntryChipButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    leadText: String,
) {
    val contentColor = LocalContentColor.current

    AssistChip(
        onClick = onClick,
        modifier = Modifier
            .height(AssistChipDefaults.Height),
        label = {
            Text(
                text = leadText,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )
}
