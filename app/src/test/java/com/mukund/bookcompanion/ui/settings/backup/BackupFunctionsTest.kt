package com.mukund.bookcompanion.ui.settings.backup

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.data.FakeBooksRepository
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
    private val fakeRepo = FakeBooksRepository()
    private lateinit var vm: BooksViewModel

    @Before
    fun setUp() {
        vm = BooksViewModel(
            fakeRepo,
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
        importBackupFile(vm, mockResolver, mockUri)
        val received = fakeRepo.insertAllSignal.await()
        assertEquals(listOf(book), received)
    }

    @Test
    fun importBackupFile_withEmptyJsonArray_insertsEmptyList() = runTest {
        every { mockResolver.openInputStream(mockUri) } returns "[]".byteInputStream()
        importBackupFile(vm, mockResolver, mockUri)
        val received = fakeRepo.insertAllSignal.await()
        assertTrue(received.isEmpty())
    }

    @Test
    fun importBackupFile_withMalformedJson_doesNotCrash() {
        // JSON literal "null" causes Gson.fromJson to return null, triggering ?: return
        every { mockResolver.openInputStream(mockUri) } returns "null".byteInputStream()
        importBackupFile(vm, mockResolver, mockUri)
        assertNull(fakeRepo.lastInsertedAll)
    }

    @Test
    fun importBackupFile_withNullInputStream_returnsEarly() {
        every { mockResolver.openInputStream(mockUri) } returns null
        importBackupFile(vm, mockResolver, mockUri)
        assertNull(fakeRepo.lastInsertedAll)
    }

    // ─── C-5: performBackup ─────────────────────────────────────────────────

    @Test
    fun performBackup_withValidOutputStream_writesJsonBytes() {
        val books = listOf(testBook)
        val outputStream = ByteArrayOutputStream()
        every { mockResolver.openOutputStream(mockUri) } returns outputStream
        performBackup(mockResolver, books, mockUri)
        val written = outputStream.toByteArray().toString(Charsets.UTF_8)
        assertTrue(written.contains("Test book"))
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
        val received = fakeRepo.insertAllSignal.await()
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
}
