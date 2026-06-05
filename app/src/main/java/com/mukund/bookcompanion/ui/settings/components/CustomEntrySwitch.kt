package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@Composable
fun CustomEntrySwitch(
    leadText: String,
    subText: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    painter: Painter? = null,
    contentDescription: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .semantics {
                role = Role.Switch
                toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
            }
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        painter?.let {
            Icon(
                painter = it,
                contentDescription = contentDescription,
                tint = bookColors.inkFaint,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = leadText,
                style = AppType.body,
                color = bookColors.ink,
            )
            subText?.let {
                Text(
                    text = it,
                    style = AppType.labelMicroMono,
                    color = bookColors.inkFaint,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                checkedTrackColor = bookColors.ink,
                checkedThumbColor = bookColors.paper,
                checkedIconColor = bookColors.ink,
                uncheckedTrackColor = bookColors.ruleSoft,
                uncheckedThumbColor = bookColors.inkFaint,
                uncheckedBorderColor = bookColors.rule,
            ),
            thumbContent = if (checked) {
                {
                    Icon(
                        painter = painterResource(R.drawable.check),
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else null,
        )
    }
    HorizontalDivider(color = bookColors.ruleSoft, thickness = 0.5.dp)
}
