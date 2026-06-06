package com.mukund.bookcompanion.ui.home

import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.data.FakeBooksRepository
import com.mukund.bookcompanion.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BooksViewModelGetBooksTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepo = FakeBooksRepository()
    private val vm = BooksViewModel(fakeRepo)

    @Test
    fun getBooks_beforeCall_booksIsEmptyList() {
        assertTrue(vm.books.isEmpty())
    }

    @Test
    fun getBooks_afterRepositoryEmission_booksMatchesEmittedList() {
        val bookList = listOf(Constants.testBook)
        fakeRepo.emitBooks(bookList)
        vm.getBooks()
        assertEquals(bookList, vm.books)
    }

    @Test
    fun getBooks_repositoryEmitsEmpty_booksRemainsEmpty() {
        vm.getBooks()
        fakeRepo.emitBooks(emptyList())
        assertTrue(vm.books.isEmpty())
    }
}
