package com.mukund.bookcompanion.domain.repository

import android.content.Context
import android.net.Uri
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE
import com.mukund.bookcompanion.data.network.BookDbProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class BooksBackupRepo (
    private val provider: BookDbProvider,
    private val context: Context,
    private val mutex: Mutex,
    private val scope: CoroutineScope,
    private val dispatcher: ExecutorCoroutineDispatcher,
) {
    suspend fun export(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                provider.close()
                context.contentResolver.openOutputStream(uri)?.use {stream ->
                    context.getDatabasePath(BOOK_TABLE).inputStream().copyTo(stream)
                }
            }
        }
    }

    suspend fun import(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                provider.close()
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val dbFile = context.getDatabasePath(BOOK_TABLE)
                    dbFile?.delete()
                    stream.copyTo(dbFile.outputStream())
                }
            }
        }
    }
}