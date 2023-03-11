package com.mukund.bookcompanion.ui.settings.backup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Backup_Screen(
    viewModel:BooksViewModel = hiltViewModel(),
    backPress: () -> Boolean
) {
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*"),
        onResult = {uri ->
            if (uri != null) viewModel.onExport(uri)
        }
    )

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {uri ->
            if (uri != null) viewModel.onImport(uri)
        }
    )
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
            LazyColumn() {
                item {
                    CustomEntryButton(
                        onClick = {
                            exportLauncher.launch(Constants.DEFAULT_EXPORT_NAME)
                            Toast.makeText(context, "Export successful", Toast.LENGTH_SHORT).show()
                        },
                        leadText = "Create local backup",
                        subText = "Data is stored locally, will be deleted upon uninstallation"
                    )
                    CustomEntryButton(
                        onClick = {
                            importLauncher.launch(arrayOf("*/*"))
                            Toast.makeText(context, "Import successful", Toast.LENGTH_SHORT).show()
                        },
                        leadText = "Restore from file",
                        subText = "Restore from compatible file"
                    )
                }
            }
        }
    }
}