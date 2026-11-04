package com.mukund.bookcompanion.ui.settings.backup

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.data.FakeBooksDao
import com.mukund.bookcompanion.data.testBook
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class BackupFunctionsTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val mockResolver = mockk<ContentResolver>(relaxed = true)
    private val mockUri = mockk<Uri>()
    private val fakeDao = FakeBooksDao()
    private lateinit var vm: BooksViewModel

    @Before
    fun setUp() {
        vm = BooksViewModel(
            fakeDao,
            PreferenceDataStoreFactory.create(
                scope = CoroutineScope(mainDispatcherRule.testDispatcher),
                produceFile = { tempFolder.newFile("prefs_${System.nanoTime()}.preferences_pb") }
            )
        )
    }

    // ─── C-3: importBackupFile ───────────────────────────────────────────────

    @Test
    fun importBackupFile_withValidJson_insertsCorrectBooks() = runTest {
        val book = testBook
        val json = """[{"id":0,"title":"Test book","author":"Test book author","year":2025,"status":"Read","genre":"Fiction","isbn":"123456"}]"""
        every { mockResolver.openInputStream(mockUri) } returns json.byteInputStream()
        val result = importBackupFile(vm, mockResolver, mockUri)
        assertEquals(listOf(book), fakeDao.lastInsertedAll)
        assertEquals(ImportResult.Success(1), result)
    }

    @Test
    fun importBackupFile_withMalformedJson_returnsMalformed() = runTest {
        every { mockResolver.openInputStream(mockUri) } returns "null".byteInputStream()
        val result = importBackupFile(vm, mockResolver, mockUri)
        assertEquals(ImportResult.Malformed, result)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_withBrokenJson_returnsMalformed() = runTest {
        every { mockResolver.openInputStream(mockUri) } returns "{not valid".byteInputStream()
        val result = importBackupFile(vm, mockResolver, mockUri)
        assertEquals(ImportResult.Malformed, result)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_withMissingRequiredFields_returnsMalformed() = runTest {
        // Gson reflections-fill missing keys, so partial JSON parses into Books with null
        // "non-null" fields. These must be rejected instead of failing at the DB write.
        val json = """[{"id":1,"author":"Some author","year":2020,"status":"Read"}]"""
        every { mockResolver.openInputStream(mockUri) } returns json.byteInputStream()
        val result = importBackupFile(vm, mockResolver, mockUri)
        assertEquals(ImportResult.Malformed, result)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_withNullAuthor_returnsMalformed() = runTest {
        val json = """[{"id":1,"title":"Title","author":null,"year":2020,"status":"Read"}]"""
        every { mockResolver.openInputStream(mockUri) } returns json.byteInputStream()
        val result = importBackupFile(vm, mockResolver, mockUri)
        assertEquals(ImportResult.Malformed, result)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_withNullStream_returnsEmptyStream() = runTest {
        every { mockResolver.openInputStream(mockUri) } returns null
        val result = importBackupFile(vm, mockResolver, mockUri)
        assertEquals(ImportResult.EmptyStream, result)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_stopsReadingAfterLimitAndReturnsFileTooLarge() = runTest {
        val sourceSize = 11 * 1024 * 1024
        var bytesRead = 0
        val oversize = object : InputStream() {
            override fun read(): Int {
                if (bytesRead == sourceSize) return -1
                bytesRead++
                return '['.code
            }

            override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
                if (bytesRead == sourceSize) return -1
                val count = minOf(length, sourceSize - bytesRead)
                buffer.fill('['.code.toByte(), offset, offset + count)
                bytesRead += count
                return count
            }
        }
        every { mockResolver.openInputStream(mockUri) } returns oversize

        val result = importBackupFile(vm, mockResolver, mockUri)

        assertEquals(ImportResult.FileTooLarge, result)
        assertEquals(10 * 1024 * 1024 + 1, bytesRead)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_acceptsInputsJustUnderAndExactlyAtLimit() = runTest {
        for (size in listOf(10 * 1024 * 1024 - 1, 10 * 1024 * 1024)) {
            every { mockResolver.openInputStream(mockUri) } returns validEmptyBackupStream(size)

            assertEquals(ImportResult.Success(0), importBackupFile(vm, mockResolver, mockUri))
            assertEquals(emptyList<Book>(), fakeDao.lastInsertedAll)
        }
    }

    @Test
    fun importBackupFile_withEmptyJsonArray_insertsEmptyList() = runTest {
        every { mockResolver.openInputStream(mockUri) } returns "[]".byteInputStream()
        importBackupFile(vm, mockResolver, mockUri)
        assertTrue(fakeDao.lastInsertedAll.orEmpty().isEmpty())
    }

    @Test
    fun importBackupFile_withMalformedJson_doesNotCrash() = runTest {
        // JSON literal "null" causes Gson.fromJson to return null, triggering ?: return
        every { mockResolver.openInputStream(mockUri) } returns "null".byteInputStream()
        importBackupFile(vm, mockResolver, mockUri)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_withNullInputStream_returnsEarly() = runTest {
        every { mockResolver.openInputStream(mockUri) } returns null
        importBackupFile(vm, mockResolver, mockUri)
        assertNull(fakeDao.lastInsertedAll)
    }

    @Test
    fun importBackupFile_whenDatabaseInsertFails_returnsRestoreFailed() = runTest {
        fakeDao.insertAllFailure = IllegalStateException("database write failed")
        every { mockResolver.openInputStream(mockUri) } returns "[]".byteInputStream()

        val result = importBackupFile(vm, mockResolver, mockUri)

        assertEquals(ImportResult.RestoreFailed, result)
    }

    // ─── C-5: performBackup ─────────────────────────────────────────────────

    @Test
    fun performBackup_withValidOutputStream_writesJsonBytes() {
        val books = listOf(testBook)
        val outputStream = ByteArrayOutputStream()
        every { mockResolver.openOutputStream(mockUri) } returns outputStream
        val result = performBackup(mockResolver, books, mockUri)
        val written = outputStream.toByteArray().toString(Charsets.UTF_8)
        assertTrue(written.contains("Test book"))
        assertEquals(ExportResult.Success, result)
    }

    @Test
    fun performBackup_withNullOutputStream_returnsNoOutputStream() {
        every { mockResolver.openOutputStream(mockUri) } returns null
        val result = performBackup(mockResolver, listOf(testBook), mockUri)
        assertEquals(ExportResult.NoOutputStream, result)
    }

    @Test
    fun performBackup_withEmptyLibrary_writesEmptyJsonArray() {
        val outputStream = ByteArrayOutputStream()
        every { mockResolver.openOutputStream(mockUri) } returns outputStream
        performBackup(mockResolver, emptyList(), mockUri)
        assertEquals("[]", outputStream.toByteArray().toString(Charsets.UTF_8))
    }

    @Test
    fun performBackup_withNullOutputStream_doesNotCrash() {
        every { mockResolver.openOutputStream(mockUri) } returns null
        performBackup(mockResolver, listOf(testBook), mockUri)
        // no assertion — test passes if no exception is thrown
    }

    @Test
    fun performBackup_roundtrip_booksIdenticalAfterImport() = runTest {
        val books = (1..5).map { i ->
            Book(
                id = i, title = "Book $i", author = "Author $i",
                year = 2000L + i, status = "Read", genre = "Fiction", isbn = "isbn$i"
            )
        }
        // Capture performBackup output
        val outputStream = ByteArrayOutputStream()
        every { mockResolver.openOutputStream(mockUri) } returns outputStream
        performBackup(mockResolver, books, mockUri)

        // Feed captured bytes back through importBackupFile
        val capturedBytes = outputStream.toByteArray()
        every { mockResolver.openInputStream(mockUri) } returns ByteArrayInputStream(capturedBytes)
        importBackupFile(vm, mockResolver, mockUri)
        val received = fakeDao.insertAllSignal.await()
        assertEquals(books, received)
    }

    // ─── M-3: createBackupIntent ────────────────────────────────────────────

    @Test
    fun createBackupIntent_filenameMatchesExpectedPattern() {
        val intent = createBackupIntent()
        val title = intent.getStringExtra(Intent.EXTRA_TITLE)
        assertNotNull(title)
        assertTrue(
            "Filename '$title' does not match expected pattern",
            title!!.matches(Regex("bookCompanion_backup_\\d{14}\\.json"))
        )
    }

    @Test
    fun createBackupIntent_actionIsCreateDocument() {
        val intent = createBackupIntent()
        assertEquals(Intent.ACTION_CREATE_DOCUMENT, intent.action)
    }

    @Test
    fun createBackupIntent_typeIsApplicationJson() {
        val intent = createBackupIntent()
        assertEquals("application/json", intent.type)
    }

    private fun validEmptyBackupStream(size: Int): InputStream = object : InputStream() {
        private var position = 0

        override fun read(): Int {
            if (position >= size) return -1
            return when (position++) {
                0 -> '['.code
                1 -> ']'.code
                else -> ' '.code
            }
        }

        override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
            if (position >= size) return -1
            val count = minOf(length, size - position)
            repeat(count) { index ->
                buffer[offset + index] = when (position++) {
                    0 -> '['.code.toByte()
                    1 -> ']'.code.toByte()
                    else -> ' '.code.toByte()
                }
            }
            return count
        }
    }
}
