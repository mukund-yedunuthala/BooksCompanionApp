package com.mukund.bookscompanion.ui.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(backPress: () -> Boolean) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Settings")
                },
                navigationIcon = {
                    IconButton(onClick = { backPress.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Return to home"
                        )
                    }
                },
            )
        },
        content = {paddingValues ->
            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    )
}