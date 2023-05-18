package com.mukund.bookcompanion.ui.settings.backup

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Backup_Screen(
    viewModel:BooksViewModel = hiltViewModel(),
    backPress: () -> Boolean
) {
    val context = LocalContext.current
    val resolver = context.contentResolver
    val books = viewModel.books

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*"),
        onResult = { uri ->
            if (uri != null) viewModel.onExport(uri)
        }
    )

    val importLauncher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) viewModel.onImport(uri)
        }
    )
    val importUriState = remember { mutableStateOf<Uri?>(null) }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            importUriState.value = uri
            importBackupFile(viewModel, context.contentResolver, uri)
            // Show a success message or perform any additional operations
            Toast.makeText(context, "Import successful", Toast.LENGTH_SHORT).show()
        }
    }
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
    ) { paddingValues ->
        Surface(Modifier.padding(paddingValues)) {
            LazyColumn() {
                item {
                    CustomEntryButton(
                        onClick = {
                            //exportLauncher.launch(Constants.DEFAULT_EXPORT_NAME)
                            //Toast.makeText(context, "Export successful", Toast.LENGTH_SHORT).show()
                            performBackup(context, resolver, books)
                        },
                        leadText = "Create local backup",
                        subText = "Data is stored locally, will be deleted upon uninstallation"
                    )
                    CustomEntryButton(
                        onClick = {
                            importLauncher.launch("application/json")
                            //importLauncher.launch(arrayOf("*/*"))

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

        // Perform the necessary logic to insert the books into your Room database
        // Example: database.bookDao().insertAll(books.toList())
        viewModel.insertAllBooks(books.toList())
    }
}

fun performBackup(context: Context, resolver: ContentResolver, books: List<Book>) {
    val backupFileName = "backup.json"
    val backupFileMimeType = "application/json"
    val gson = Gson()
    val backupData = gson.toJson(books)

    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, backupFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, backupFileMimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }
    val collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    val uri = resolver.insert(collection, values)
    uri?.let { backupUri ->
        resolver.openOutputStream(backupUri)?.use { outputStream ->
            outputStream.write(backupData.toByteArray())
        }
        // success toast
        Toast.makeText(context,"Export successful", Toast.LENGTH_SHORT).show()
    }
}


private fun importBackupFile(inputStream: InputStream) {
    val backupData = inputStream.bufferedReader().use(BufferedReader::readText)

    val gson = Gson()
    val books = gson.fromJson(backupData, Array<Book>::class.java)

    // Perform the necessary logic to insert the books into your Room database
    // Example: database.bookDao().insertAll(books.toList())
}