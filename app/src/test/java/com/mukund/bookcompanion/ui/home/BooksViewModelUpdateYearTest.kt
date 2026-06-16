package com.mukund.bookcompanion.ui.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
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
class BooksViewModelUpdateYearTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var vm: BooksViewModel

    @Before
    fun setUp() {
        vm = BooksViewModel(
            FakeBooksDao(),
            PreferenceDataStoreFactory.create(
                scope = CoroutineScope(mainDispatcherRule.testDispatcher),
                produceFile = { tempFolder.newFile("prefs_${System.nanoTime()}.preferences_pb") }
            )
        )
    }

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
