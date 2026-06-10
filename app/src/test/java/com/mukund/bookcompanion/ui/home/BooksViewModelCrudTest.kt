package com.mukund.bookcompanion.ui.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.data.FakeBooksRepository
import com.mukund.bookcompanion.data.testBook
import com.mukund.bookcompanion.util.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelCrudTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val fakeRepo = FakeBooksRepository()
    private lateinit var vm: BooksViewModel

    private val book = testBook

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

    @Test
    fun addBook_callsRepositoryAddBook_withCorrectBook() = runTest {
        // addBook returns Job (expression body = viewModelScope.launch); join it to wait for IO
        vm.addBook(book).join()
        assertEquals(book, fakeRepo.lastAdded)
    }

    @Test
    fun updateBook_callsRepositoryUpdateBook_withCorrectBook() = runTest {
        vm.updateBook(book).join()
        assertEquals(book, fakeRepo.lastUpdated)
    }

    @Test
    fun deleteBook_callsRepositoryDeleteBook_withCorrectBook() = runTest {
        vm.deleteBook(book).join()
        assertEquals(book, fakeRepo.lastDeleted)
    }

    @Test
    fun insertAllBooks_callsRepositoryInsertAll_withGivenList() = runTest {
        val books = listOf(book)
        vm.insertAllBooks(books)
        // insertAllBooks() returns Unit; await the fake's signal set when the repo method runs
        val received = fakeRepo.insertAllSignal.await()
        assertEquals(books, received)
        assertEquals(books, fakeRepo.lastInsertedAll)
    }
}
