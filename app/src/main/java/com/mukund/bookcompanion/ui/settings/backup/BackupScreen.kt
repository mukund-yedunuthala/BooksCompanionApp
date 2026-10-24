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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.gson.Gson
import com.mukund.bookcompanion.domain.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.IOException
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

    var pendingRestoreUri by remember { mutableStateOf<Uri?>(null) }
    val importLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                pendingRestoreUri = uri
            }
        }
    val activityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let { backupUri ->
                    scope.launch(Dispatchers.IO) {
                        val result = try {
                            val books = viewModel.getBooksSnapshot()
                            performBackup(resolver, books, backupUri)
                        } catch (cancelled: CancellationException) {
                            throw cancelled
                        } catch (_: Exception) {
                            ExportResult.Failure
                        }
                        val message = when (result) {
                            ExportResult.Success ->
                                context.getString(R.string.backup_export_success)
                            ExportResult.NoOutputStream, ExportResult.Failure ->
                                context.getString(R.string.backup_export_error)
                            ExportResult.FileTooLarge ->
                                context.getString(R.string.backup_export_error_too_large)
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

    pendingRestoreUri?.let { uri ->
        AlertDialog(
            onDismissRequest = { pendingRestoreUri = null },
            containerColor = bookColors.paper,
            title = { Text(stringResource(R.string.backup_restore_warning_title)) },
            text = { Text(stringResource(R.string.backup_restore_warning_body)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingRestoreUri = null
                        scope.launch(Dispatchers.IO) {
                            val result = try {
                                importBackupFile(viewModel, resolver, uri)
                            } catch (cancelled: CancellationException) {
                                throw cancelled
                            } catch (_: Exception) {
                                ImportResult.IoFailure
                            }
                            val message = when (result) {
                                is ImportResult.Success ->
                                    context.getString(R.string.backup_import_success, result.count)
                                ImportResult.EmptyStream, ImportResult.IoFailure ->
                                    context.getString(R.string.backup_import_error_unreadable)
                                ImportResult.FileTooLarge ->
                                    context.getString(R.string.backup_import_error_too_large)
                                ImportResult.Malformed ->
                                    context.getString(R.string.backup_import_error_invalid)
                                ImportResult.RestoreFailed ->
                                    context.getString(R.string.backup_import_error_restore)
                            }
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) { Text(stringResource(R.string.backup_restore_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { pendingRestoreUri = null }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

private const val MAX_BACKUP_BYTES = 10 * 1024 * 1024  // 10 MB

/** Outcome of a restore so the caller can give the user honest feedback. */
sealed interface ImportResult {
    data class Success(val count: Int) : ImportResult
    data object EmptyStream : ImportResult
    data object IoFailure : ImportResult
    data object FileTooLarge : ImportResult
    data object Malformed : ImportResult
    data object RestoreFailed : ImportResult
}

/** Outcome of an export. */
sealed interface ExportResult {
    data object Success : ExportResult
    data object NoOutputStream : ExportResult
    data object FileTooLarge : ExportResult
    data object Failure : ExportResult
}

suspend fun importBackupFile(
    viewModel: BooksViewModel,
    contentResolver: ContentResolver,
    uri: Uri,
): ImportResult {
    return try {
        val inputStream = contentResolver.openInputStream(uri) ?: return ImportResult.EmptyStream
        inputStream.use { stream ->
            val bytes = readBackupBytes(stream) ?: return ImportResult.FileTooLarge
            val backupData = bytes.decodeToString()
            val books = try {
                Gson().fromJson(backupData, Array<Book>::class.java) ?: return ImportResult.Malformed
            } catch (e: com.google.gson.JsonSyntaxException) {
                return ImportResult.Malformed
            }
            try {
                viewModel.insertAllBooks(books.toList())
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                return ImportResult.RestoreFailed
            }
            ImportResult.Success(books.size)
        }
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (_: IOException) {
        ImportResult.IoFailure
    } catch (_: SecurityException) {
        ImportResult.IoFailure
    } catch (_: Exception) {
        ImportResult.IoFailure
    }
}

private fun readBackupBytes(stream: InputStream): ByteArray? {
    val output = ByteArrayOutputStream()
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var totalBytes = 0

    while (true) {
        val remainingIncludingOverflowByte = MAX_BACKUP_BYTES + 1 - totalBytes
        val bytesRead = stream.read(buffer, 0, minOf(buffer.size, remainingIncludingOverflowByte))
        if (bytesRead < 0) break
        if (bytesRead == 0) {
            val nextByte = stream.read()
            if (nextByte < 0) break
            if (totalBytes == MAX_BACKUP_BYTES) return null
            output.write(nextByte)
            totalBytes++
            continue
        }
        totalBytes += bytesRead
        if (totalBytes > MAX_BACKUP_BYTES) return null
        output.write(buffer, 0, bytesRead)
    }

    return output.toByteArray()
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
    val backupData = Gson().toJson(books).toByteArray(Charsets.UTF_8)
    if (backupData.size > MAX_BACKUP_BYTES) return ExportResult.FileTooLarge
    val outputStream = resolver.openOutputStream(backupUri) ?: return ExportResult.NoOutputStream
    outputStream.use { stream ->
        stream.write(backupData)
    }
    return ExportResult.Success
}
