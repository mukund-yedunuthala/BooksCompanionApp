package com.mukund.bookcompanion.ui.settings.backup

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton
import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.mukund.bookcompanion.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Backup_Screen(
    viewModel: BooksViewModel = hiltViewModel(),
    backPress: () -> Boolean
) {
    val context = LocalContext.current
    val resolver = context.contentResolver
    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }


    val importUriState = remember { mutableStateOf<Uri?>(null) }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            importUriState.value = uri
            importBackupFile(viewModel, context.contentResolver, uri)
            // Show a success message or perform any additional operations
            Toast.makeText(context, "Import successful", Toast.LENGTH_SHORT).show()
        }
    }
    val activityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let { backupUri ->
                performBackup(context, resolver, viewModel.books, backupUri)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Backup & Restore") },
                navigationIcon = {
                    IconButton(onClick = { backPress.invoke() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Return to settings",
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(Modifier.padding(paddingValues)) {
            LazyColumn() {
                item {
                    CustomEntryButton(
                        onClick = {
                            activityResultLauncher.launch(createBackupIntent())
                        },
                        leadText = "Create local backup",
                        subText = "Data is stored locally, will be deleted upon uninstallation"
                    )
                    CustomEntryButton(
                        onClick = {
                            importLauncher.launch("application/json")
                        },
                        leadText = "Restore from file",
                        subText = "Restore from compatible file"
                    )
                }
            }
        }
    }
}

fun importBackupFile(viewModel: BooksViewModel, contentResolver: ContentResolver, uri: Uri) {
    val inputStream = contentResolver.openInputStream(uri)
    inputStream?.use { stream ->
        val backupData = stream.bufferedReader().use(BufferedReader::readText)
        val gson = Gson()
        val books = gson.fromJson(backupData, Array<Book>::class.java)
        viewModel.insertAllBooks(books.toList())
    }
}
fun createBackupIntent(): Intent {
    val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
    val defaultFileName = "bookCompanion_backup_$timestamp.json"
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "application/json"
    intent.putExtra(Intent.EXTRA_TITLE, defaultFileName)

    return intent
}
fun performBackup(context: Context, resolver: ContentResolver, books: List<Book>, backupUri: Uri) {
    resolver.openOutputStream(backupUri)?.use { outputStream ->
        val gson = Gson()
        val backupData = gson.toJson(books)
        outputStream.write(backupData.toByteArray())
    }
    // success toast
    Toast.makeText(context, "Export successful", Toast.LENGTH_SHORT).show()

}