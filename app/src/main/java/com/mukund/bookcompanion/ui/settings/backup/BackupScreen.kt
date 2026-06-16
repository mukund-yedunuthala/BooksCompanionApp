package com.mukund.bookcompanion.ui.settings.backup

import com.mukund.bookcompanion.design.BookCompanionBorders
import com.mukund.bookcompanion.design.BookCompanionSpacing
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.mukund.bookcompanion.domain.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton
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
                    val result = importBackupFile(viewModel, context.contentResolver, uri)
                    val message = when (result) {
                        is ImportResult.Success ->
                            context.getString(R.string.backup_import_success, result.count)
                        ImportResult.EmptyStream ->
                            context.getString(R.string.backup_import_error_unreadable)
                        ImportResult.FileTooLarge ->
                            context.getString(R.string.backup_import_error_too_large)
                        ImportResult.Malformed ->
                            context.getString(R.string.backup_import_error_invalid)
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                        val result = performBackup(resolver, viewModel.books, backupUri)
                        val message = when (result) {
                            ExportResult.Success ->
                                context.getString(R.string.backup_export_success)
                            ExportResult.NoOutputStream ->
                                context.getString(R.string.backup_export_error)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                        .padding(horizontal = BookCompanionSpacing.gutter)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                role = Role.Button
                            ) { backPress.invoke() }
                            .heightIn(min = 48.dp)
                            .padding(top = 12.dp, bottom = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.backup_back_description),
                            tint = bookColors.inkSoft,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(R.string.backup_back_to_settings),
                            style = AppType.bodySmall,
                            color = bookColors.inkSoft,
                        )
                    }

                    Text(
                        text = stringResource(R.string.backup_title),
                        style = AppType.displaySerifItalic,
                        color = bookColors.ink,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    HorizontalDivider(
                        color = bookColors.rule,
                        thickness = BookCompanionBorders.hairline,
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
                    leadText = stringResource(R.string.backup_create),
                    subText = stringResource(R.string.backup_create_subtext)
                )
                CustomEntryButton(
                    onClick = { importLauncher.launch("application/json") },
                    leadText = stringResource(R.string.backup_restore),
                    subText = stringResource(R.string.backup_restore_subtext)
                )
            }
        }
    }
}

private const val MAX_IMPORT_BYTES = 10 * 1024 * 1024  // 10 MB

/** Outcome of a restore so the caller can give the user honest feedback. */
sealed interface ImportResult {
    data class Success(val count: Int) : ImportResult
    data object EmptyStream : ImportResult
    data object FileTooLarge : ImportResult
    data object Malformed : ImportResult
}

/** Outcome of an export. */
sealed interface ExportResult {
    data object Success : ExportResult
    data object NoOutputStream : ExportResult
}

fun importBackupFile(
    viewModel: BooksViewModel,
    contentResolver: ContentResolver,
    uri: Uri,
): ImportResult {
    val inputStream = contentResolver.openInputStream(uri) ?: return ImportResult.EmptyStream
    inputStream.use { stream ->
        val bytes = stream.readBytes()
        if (bytes.size > MAX_IMPORT_BYTES) return ImportResult.FileTooLarge
        val backupData = bytes.decodeToString()
        val books = try {
            Gson().fromJson(backupData, Array<Book>::class.java) ?: return ImportResult.Malformed
        } catch (e: com.google.gson.JsonSyntaxException) {
            return ImportResult.Malformed
        }
        viewModel.insertAllBooks(books.toList())
        return ImportResult.Success(books.size)
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
fun performBackup(resolver: ContentResolver, books: List<Book>, backupUri: Uri): ExportResult {
    val outputStream = resolver.openOutputStream(backupUri) ?: return ExportResult.NoOutputStream
    outputStream.use { stream ->
        val gson = Gson()
        val backupData = gson.toJson(books)
        stream.write(backupData.toByteArray())
    }
    return ExportResult.Success
}