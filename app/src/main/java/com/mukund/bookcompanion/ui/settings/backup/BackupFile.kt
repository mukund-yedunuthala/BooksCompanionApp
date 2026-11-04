package com.mukund.bookcompanion.ui.settings.backup

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BooksViewModel
import kotlinx.coroutines.CancellationException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            } catch (e: JsonSyntaxException) {
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
