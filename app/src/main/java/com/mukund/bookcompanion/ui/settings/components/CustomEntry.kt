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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@Composable
fun CustomEntryButton(
    onClick: () -> Unit,
    leadText: String,
    subText: String? = null,
    painter: Painter? = null,
    contentDescription: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics { role = Role.Button }
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
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = null,
            tint = bookColors.rule,
            modifier = Modifier
                .size(14.dp)
                .rotate(180f)
        )
    }
    HorizontalDivider(color = bookColors.ruleSoft, thickness = 0.5.dp)
}