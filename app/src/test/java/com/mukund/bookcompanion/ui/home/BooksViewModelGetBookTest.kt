package com.mukund.bookcompanion.ui.home

import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.data.FakeBooksRepository
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BooksViewModelGetBookTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepo = FakeBooksRepository()
    private val vm = BooksViewModel(fakeRepo)

    private val defaultBook = Book(
        id = 0, title = NO_VALUE, genre = NO_VALUE, isbn = NO_VALUE,
        author = NO_VALUE, year = 0, status = NO_VALUE
    )

    @Test
    fun getBook_withValidId_updatesBookState() {
        fakeRepo.emitBook(Constants.testBook)
        vm.getBook(Constants.testBook.id)
        assertEquals(Constants.testBook, vm.book)
    }

    @Test
    fun getBook_withNullEmission_doesNotOverwriteExistingState() {
        fakeRepo.emitBook(Constants.testBook)
        vm.getBook(Constants.testBook.id)
        fakeRepo.emitBook(null)
        assertEquals(Constants.testBook, vm.book)
    }

    @Test
    fun getBook_withInvalidId_stateRemainsDefault() {
        // bookFlow starts as null; collecting null does nothing to state
        vm.getBook(999)
        assertEquals(defaultBook, vm.book)
    }
}
