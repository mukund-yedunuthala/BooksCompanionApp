package com.mukund.bookcompanion.ui.home

import com.mukund.bookcompanion.data.FakeBooksRepository
import com.mukund.bookcompanion.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BooksViewModelUpdateYearTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val vm = BooksViewModel(FakeBooksRepository())

    @Test
    fun updateYear_withValidNumericString_updatesBookYear() {
        vm.updateYear("2023")
        assertEquals(2023L, vm.book.year)
    }

    @Test
    fun updateYear_withEmptyString_doesNotUpdateYear() {
        vm.updateYear("2020")
        vm.updateYear("")
        assertEquals(2020L, vm.book.year)
    }

    @Test
    fun updateYear_withNonNumericString_doesNotCrash() {
        vm.updateYear("2020")
        vm.updateYear("20a5")
        assertEquals(2020L, vm.book.year)
    }

    @Test
    fun updateYear_withWhitespaceOnly_doesNotCrash() {
        vm.updateYear("2020")
        vm.updateYear("  ")
        assertEquals(2020L, vm.book.year)
    }
}
