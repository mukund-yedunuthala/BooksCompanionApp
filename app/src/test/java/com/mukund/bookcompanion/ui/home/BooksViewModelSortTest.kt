package com.mukund.bookcompanion.ui.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.data.FakeBooksRepository
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.util.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

// bookA: alphabetically first, chronologically older
// bookB: alphabetically last, chronologically newer
// title-sort order: [bookA, bookB]; year-sort order: [bookB, bookA]
private val bookA = Book(id = 1, title = "Aardvark", author = "A", year = 2010,
    status = "Read", genre = "Fiction", isbn = "111")
private val bookB = Book(id = 2, title = "Zebra", author = "Z", year = 2020,
    status = "Unread", genre = "Non-fiction", isbn = "222")

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelSortTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

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

    @Test
    fun defaultSortOption_isTitle() {
        assertEquals(SortOption.TITLE, vm.sortOption)
    }

    @Test
    fun updateSortOption_title_booksAreSortedByTitleAsc() {
        fakeRepo.emitBooks(listOf(bookB, bookA))
        vm.updateSortOption(SortOption.TITLE)
        assertEquals(listOf(bookA, bookB), vm.books)
    }

    @Test
    fun updateSortOption_year_booksAreSortedByYearDesc() {
        fakeRepo.emitBooks(listOf(bookA, bookB))
        vm.updateSortOption(SortOption.YEAR)
        assertEquals(listOf(bookB, bookA), vm.books)
    }

    @Test
    fun updateSortOption_toggleTwice_booksReflectLatestSort() {
        fakeRepo.emitBooks(listOf(bookA, bookB))
        vm.updateSortOption(SortOption.YEAR)
        assertEquals(listOf(bookB, bookA), vm.books)
        vm.updateSortOption(SortOption.TITLE)
        assertEquals(listOf(bookA, bookB), vm.books)
    }
}
