package com.mukund.bookcompanion.ui.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.data.testBook
import com.mukund.bookcompanion.data.FakeBooksDao
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

    @Test
    fun getBook_withValidId_reportsLoaded() {
        fakeDao.emitBook(testBook)
        vm.getBook(testBook.id)
        assertEquals(BookDetailState.Loaded(testBook), vm.bookDetail)
    }

    @Test
    fun getBook_withNullEmission_reportsNotFound() {
        fakeDao.emitBook(testBook)
        vm.getBook(testBook.id)
        fakeDao.emitBook(null)
        assertEquals(BookDetailState.NotFound, vm.bookDetail)
    }

    @Test
    fun getBook_withInvalidId_reportsNotFound() {
        // Flow for an unknown id starts as null.
        vm.getBook(999)
        assertEquals(BookDetailState.NotFound, vm.bookDetail)
    }

    @Test
    fun getBook_repeatedCall_onlyLatestBookFlowUpdatesDetailState() {
        val firstBook = testBook.copy(id = 1)
        val secondBook = testBook.copy(id = 2)

        vm.getBook(firstBook.id)
        vm.getBook(secondBook.id)
        fakeDao.emitBook(secondBook.id, secondBook)
        assertEquals(BookDetailState.Loaded(secondBook), vm.bookDetail)

        fakeDao.emitBook(firstBook.id, firstBook)
        assertEquals(BookDetailState.Loaded(secondBook), vm.bookDetail)
    }
}
