package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
@Composable
fun CustomEntrySwitch(
    leadText: String,
    subText: String? = null,
    boolVal: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.75f)
                .height(BUTTON_HEIGHT),
            contentAlignment = Alignment.CenterStart
        ) {
            TextButton(
                onClick = { onChange(!boolVal) },
                modifier = Modifier.height(BUTTON_HEIGHT),
                shape = RectangleShape,
            ) {
                Column(
                    Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = leadText,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(20.dp, bottom = 2.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Left
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(20.dp, top = 3.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Left
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(0.25f)
                .height(BUTTON_HEIGHT),
            contentAlignment = Alignment.CenterEnd
        ) {
            Switch(
                checked = boolVal,
                onCheckedChange = onChange,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}
