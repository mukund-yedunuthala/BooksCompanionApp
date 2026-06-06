package com.mukund.bookcompanion.ui.home

import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.data.FakeBooksRepository
import com.mukund.bookcompanion.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BooksViewModelFieldUpdateTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepo = FakeBooksRepository()
    private val vm = BooksViewModel(fakeRepo)

    /** Seed a non-default starting state so we can detect unwanted field changes. */
    private fun seedBook() {
        vm.updateTitle("Original Title")
        vm.updateAuthor("Original Author")
        vm.updateYear("1999")
        vm.updateGenre("Original Genre")
        vm.updateISBN("0000000000")
        vm.updateStatus("Unread")
    }

    @Test
    fun updateTitle_updatesOnlyTitle_otherFieldsUnchanged() {
        seedBook()
        val before = vm.book
        vm.updateTitle("New Title")
        assertEquals("New Title", vm.book.title)
        assertEquals(before.author, vm.book.author)
        assertEquals(before.year, vm.book.year)
        assertEquals(before.genre, vm.book.genre)
        assertEquals(before.isbn, vm.book.isbn)
        assertEquals(before.status, vm.book.status)
    }

    @Test
    fun updateAuthor_updatesOnlyAuthor_otherFieldsUnchanged() {
        seedBook()
        val before = vm.book
        vm.updateAuthor("New Author")
        assertEquals("New Author", vm.book.author)
        assertEquals(before.title, vm.book.title)
        assertEquals(before.year, vm.book.year)
        assertEquals(before.genre, vm.book.genre)
        assertEquals(before.isbn, vm.book.isbn)
        assertEquals(before.status, vm.book.status)
    }

    @Test
    fun updateGenre_updatesOnlyGenre_otherFieldsUnchanged() {
        seedBook()
        val before = vm.book
        vm.updateGenre("New Genre")
        assertEquals("New Genre", vm.book.genre)
        assertEquals(before.title, vm.book.title)
        assertEquals(before.author, vm.book.author)
        assertEquals(before.year, vm.book.year)
        assertEquals(before.isbn, vm.book.isbn)
        assertEquals(before.status, vm.book.status)
    }

    @Test
    fun updateISBN_updatesOnlyIsbn_otherFieldsUnchanged() {
        seedBook()
        val before = vm.book
        vm.updateISBN("9999999999")
        assertEquals("9999999999", vm.book.isbn)
        assertEquals(before.title, vm.book.title)
        assertEquals(before.author, vm.book.author)
        assertEquals(before.year, vm.book.year)
        assertEquals(before.genre, vm.book.genre)
        assertEquals(before.status, vm.book.status)
    }

    @Test
    fun updateStatus_trimsLeadingAndTrailingWhitespace() {
        vm.updateStatus("  Read  ")
        assertEquals("Read", vm.book.status)
    }

    @Test
    fun updateStatus_updatesOnlyStatus_otherFieldsUnchanged() {
        seedBook()
        val before = vm.book
        vm.updateStatus("Read")
        assertEquals("Read", vm.book.status)
        assertEquals(before.title, vm.book.title)
        assertEquals(before.author, vm.book.author)
        assertEquals(before.year, vm.book.year)
        assertEquals(before.genre, vm.book.genre)
        assertEquals(before.isbn, vm.book.isbn)
    }
}
