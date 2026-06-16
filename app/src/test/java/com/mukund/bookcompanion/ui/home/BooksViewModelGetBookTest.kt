package com.mukund.bookcompanion.ui.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.data.testBook
import com.mukund.bookcompanion.data.FakeBooksDao
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.util.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelGetBookTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

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

    private val defaultBook = Book(
        id = 0, title = NO_VALUE, genre = NO_VALUE, isbn = NO_VALUE,
        author = NO_VALUE, year = 0, status = NO_VALUE
    )

    @Test
    fun getBook_withValidId_updatesBookState() {
        fakeDao.emitBook(testBook)
        vm.getBook(testBook.id)
        assertEquals(testBook, vm.book)
    }

    @Test
    fun getBook_withNullEmission_doesNotOverwriteExistingState() {
        fakeDao.emitBook(testBook)
        vm.getBook(testBook.id)
        fakeDao.emitBook(null)
        assertEquals(testBook, vm.book)
    }

    @Test
    fun getBook_withInvalidId_stateRemainsDefault() {
        // bookFlow starts as null; collecting null does nothing to state
        vm.getBook(999)
        assertEquals(defaultBook, vm.book)
    }
}
