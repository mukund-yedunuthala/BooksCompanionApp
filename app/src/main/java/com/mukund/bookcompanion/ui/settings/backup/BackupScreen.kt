package com.mukund.bookcompanion.ui.settings.backup

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.mukund.bookcompanion.domain.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton
import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Backup_Screen(
    viewModel: BooksViewModel = hiltViewModel(),
    backPress: () -> Boolean
) {
    val context = LocalContext.current
    val resolver = context.contentResolver
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }

    val importUriState = remember { mutableStateOf<Uri?>(null) }
    val importLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                importUriState.value = uri
                scope.launch(Dispatchers.IO) {
                    importBackupFile(viewModel, context.contentResolver, uri)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Import successful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    val activityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let { backupUri ->
                    scope.launch(Dispatchers.IO) {
                        performBackup(resolver, viewModel.books, backupUri)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Export successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    Scaffold(
        containerColor = bookColors.paper,
        topBar = {
            Surface(
                color = bookColors.paper,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { backPress.invoke() }
                            .padding(top = 12.dp, bottom = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Return to settings",
                            tint = bookColors.inkSoft,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Settings",
                            style = AppType.bodySmall,
                            color = bookColors.inkSoft,
                        )
                    }

                    Text(
                        text = "Backup &\nRestore",
                        style = AppType.displaySerifItalic,
                        color = bookColors.ink,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    HorizontalDivider(
                        color = bookColors.rule,
                        thickness = 0.5.dp,
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                CustomEntryButton(
                    onClick = { activityResultLauncher.launch(createBackupIntent()) },
                    leadText = "Create local backup",
                    subText = "Data is stored locally, will be deleted upon uninstallation"
                )
                CustomEntryButton(
                    onClick = { importLauncher.launch("application/json") },
                    leadText = "Restore from file",
                    subText = "Restore from compatible file"
                )
            }
        }
    }
}

fun importBackupFile(viewModel: BooksViewModel, contentResolver: ContentResolver, uri: Uri) {
    val inputStream = contentResolver.openInputStream(uri) ?: return
    inputStream.use { stream ->
        val backupData = stream.bufferedReader().use(BufferedReader::readText)
        val books = Gson().fromJson(backupData, Array<Book>::class.java) ?: return
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
fun performBackup(resolver: ContentResolver, books: List<Book>, backupUri: Uri) {
    resolver.openOutputStream(backupUri)?.use { outputStream ->
        val gson = Gson()
        val backupData = gson.toJson(books)
        outputStream.write(backupData.toByteArray())
    }
}