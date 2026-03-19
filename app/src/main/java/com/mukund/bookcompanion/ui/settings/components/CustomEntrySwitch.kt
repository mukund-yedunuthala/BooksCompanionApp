package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.mukund.bookcompanion.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomEntrySwitch(
    leadText: String,
    subText: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    painter: Painter? = null,
    contentDescription: String? = null,
) {
    ListItem(
        headlineContent = {
            Text(
                text = leadText,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        supportingContent = subText?.let {
            { Text(text = it) }
        },
        leadingContent = painter?.let {
            {
                Icon(
                    painter = it,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                )
            }
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = null,
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
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .clickable { onCheckedChange(!checked) }
            .semantics {
                role = Role.Switch
                toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
            },
    )
}
