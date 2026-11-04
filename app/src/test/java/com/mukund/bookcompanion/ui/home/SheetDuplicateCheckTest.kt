package com.mukund.bookcompanion.ui.home

import com.mukund.bookcompanion.data.testBook
import com.mukund.bookcompanion.ui.home.components.isDuplicateEntry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The edit sheet's duplicate check (same title+author+year as another book). */
class SheetDuplicateCheckTest {

    private val dune = testBook.copy(title = "Dune", author = "Frank Herbert", year = 1965)

    @Test
    fun sameTitleAuthorYear_matches() {
        assertTrue(
            isDuplicateEntry(
                books = listOf(dune), excludeId = null,
                title = " dune ", author = " frank herbert ", year = 1965,
            )
        )
    }

    @Test
    fun editedBookExcludedById() {
        assertFalse(
            isDuplicateEntry(
                books = listOf(dune), excludeId = dune.id,
                title = "Dune", author = "Frank Herbert", year = 1965,
            )
        )
    }

    @Test
    fun differentBookStillMatchingFields_matches() {
        val otherCopy = dune.copy(id = 99)
        assertTrue(
            isDuplicateEntry(
                books = listOf(otherCopy), excludeId = dune.id,
                title = "Dune", author = "Frank Herbert", year = 1965,
            )
        )
    }

    @Test
    fun differentField_valuesDoNotMatch() {
        assertFalse(
            isDuplicateEntry(
                books = listOf(dune), excludeId = null,
                title = "Dune Messiah", author = "Frank Herbert", year = 1965,
            )
        )
        assertFalse(
            isDuplicateEntry(
                books = listOf(dune), excludeId = null,
                title = "Dune", author = "Frank Herbert", year = 1969,
            )
        )
    }
}
