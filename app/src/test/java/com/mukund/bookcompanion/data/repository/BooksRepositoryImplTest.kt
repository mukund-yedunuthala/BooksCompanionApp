package com.mukund.bookcompanion.data.repository

import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.data.network.BookDbProvider
import com.mukund.bookcompanion.data.network.BooksDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class BooksRepositoryImplTest {

    private val mockProvider = mockk<BookDbProvider>(relaxed = true)
    private val mockDao = mockk<BooksDao>(relaxed = true)
    private val repo = BooksRepositoryImpl(mockProvider)
    private val book = Constants.testBook

    @Before
    fun setUp() {
        // BooksRepositoryImpl calls provider.BooksDao() on every operation (note capital B)
        every { mockProvider.BooksDao() } returns mockDao
        every { mockDao.getBooks() } returns flowOf(emptyList())
        every { mockDao.getBook(any()) } returns flowOf(null)
    }

    @Test
    fun getBooksFromRoom_delegatesToDaoGetBooks() {
        repo.getBooksFromRoom()
        verify { mockDao.getBooks() }
    }

    @Test
    fun getBookFromRoom_delegatesToDaoGetBook_withCorrectId() {
        repo.getBookFromRoom(42)
        verify { mockDao.getBook(42) }
    }

    @Test
    fun addBookToRoom_delegatesToDaoAddBook_withCorrectBook() {
        repo.addBookToRoom(book)
        verify { mockDao.addBook(book) }
    }

    @Test
    fun updateBookInRoom_delegatesToDaoUpdateBook_withCorrectBook() {
        repo.updateBookInRoom(book)
        verify { mockDao.updateBook(book) }
    }

    @Test
    fun deleteBookFromRoom_delegatesToDaoDeleteBook_withCorrectBook() {
        repo.deleteBookFromRoom(book)
        verify { mockDao.deleteBook(book) }
    }

    @Test
    fun insertAllBooks_delegatesToDaoInsertAll_withCorrectList() {
        val books = listOf(book)
        repo.insertAllBooks(books)
        verify { mockDao.insertAll(books) }
    }
}
