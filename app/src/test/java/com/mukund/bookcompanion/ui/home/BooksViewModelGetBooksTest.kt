package com.mukund.bookcompanion.ui.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.data.FakeBooksDao
import com.mukund.bookcompanion.data.testBook
import com.mukund.bookcompanion.util.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelGetBooksTest {

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
    fun getBooks_beforeCall_booksIsEmptyList() {
        assertTrue(vm.books.isEmpty())
    }

    @Test
    fun getBooks_afterRepositoryEmission_booksMatchesEmittedList() {
        val bookList = listOf(testBook)
        fakeDao.emitBooks(bookList)
        vm.getBooks()
        assertEquals(bookList, vm.books)
    }

    @Test
    fun getBooks_repositoryEmitsEmpty_booksRemainsEmpty() {
        vm.getBooks()
        fakeDao.emitBooks(emptyList())
        assertTrue(vm.books.isEmpty())
    }
}
