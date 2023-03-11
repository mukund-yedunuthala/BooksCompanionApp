package com.mukund.bookcompanion.ui.settings.backup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Backup_Screen(backPress: () -> Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Backup & Restore") },
                navigationIcon = {
                    IconButton(onClick = { backPress.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Return to settings"
                        )
                    }
                }
            )
        }
    ) {paddingValues ->
        Surface(Modifier.padding(paddingValues)) {
            CustomEntryButton(
                onClick = {},
                leadText = "Create local backup",
                subText = "Data is stored locally, will be deleted upon uninstallation"
            )
        }
    }
}