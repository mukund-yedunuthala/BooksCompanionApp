package com.mukund.bookcompanion.ui.home

import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.data.FakeBooksRepository
import com.mukund.bookcompanion.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BooksViewModelCrudTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepo = FakeBooksRepository()
    private val vm = BooksViewModel(fakeRepo)

    private val book = Constants.testBook

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
