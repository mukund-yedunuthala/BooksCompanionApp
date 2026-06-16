package com.mukund.bookcompanion.ui.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.data.FakeBooksDao
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

    private val fakeDao = FakeBooksDao()
    private lateinit var vm: BooksViewModel

    private val book = testBook

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

    @Test
    fun addBook_callsRepositoryAddBook_withCorrectBook() = runTest {
        // addBook returns Job (expression body = viewModelScope.launch); join it to wait for IO
        vm.addBook(book).join()
        assertEquals(book, fakeDao.lastAdded)
    }

    @Test
    fun updateBook_callsRepositoryUpdateBook_withCorrectBook() = runTest {
        vm.updateBook(book).join()
        assertEquals(book, fakeDao.lastUpdated)
    }

    @Test
    fun deleteBook_callsRepositoryDeleteBook_withCorrectBook() = runTest {
        vm.deleteBook(book).join()
        assertEquals(book, fakeDao.lastDeleted)
    }

    @Test
    fun insertAllBooks_callsRepositoryInsertAll_withGivenList() = runTest {
        val books = listOf(book)
        vm.insertAllBooks(books)
        val received = fakeDao.insertAllSignal.await()
        assertEquals(books, received)
        assertEquals(books, fakeDao.lastInsertedAll)
    }
}
