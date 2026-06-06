package com.mukund.bookcompanion.ui.settings

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mukund.bookcompanion.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

    private fun createDataStore() = PreferenceDataStoreFactory.create(
        scope = TestScope(mainDispatcherRule.testDispatcher),
        produceFile = { tempFolder.newFile("test_${System.nanoTime()}.preferences_pb") }
    )

    @Test
    fun initialState_emptyDataStore_defaultsFalseForBothPrefs() = runTest {
        val store = createDataStore()
        val vm = SettingsViewModel(store)
        advanceUntilIdle()
        assertFalse(vm.hasUserDarkThemeEnabled)
        assertFalse(vm.followSystemTheme)
    }

    @Test
    fun saveUserDarkThemeEnabled_true_stateBecomesTrue() = runTest {
        val store = createDataStore()
        val vm = SettingsViewModel(store)
        vm.saveUserDarkThemeEnabled(true)
        advanceUntilIdle()
        assertTrue(vm.hasUserDarkThemeEnabled)
    }

    @Test
    fun saveUserDarkThemeEnabled_false_afterTrue_stateReverts() = runTest {
        val store = createDataStore()
        val vm = SettingsViewModel(store)
        vm.saveUserDarkThemeEnabled(true)
        advanceUntilIdle()
        vm.saveUserDarkThemeEnabled(false)
        advanceUntilIdle()
        assertFalse(vm.hasUserDarkThemeEnabled)
    }

    @Test
    fun saveUserFollowSystemEnabled_true_stateBecomesTrue() = runTest {
        val store = createDataStore()
        val vm = SettingsViewModel(store)
        vm.saveUserFollowSystemEnabled(true)
        advanceUntilIdle()
        assertTrue(vm.followSystemTheme)
    }

    @Test
    fun savedPreference_persistsAfterViewModelRecreation() = runTest {
        // In production, DataStore is a Hilt singleton — the same store is re-injected into
        // the new ViewModel. Simulate this by reusing the same store with a fresh ViewModel.
        val store = createDataStore()
        val vm1 = SettingsViewModel(store)
        vm1.saveUserDarkThemeEnabled(true)
        advanceUntilIdle()

        val vm2 = SettingsViewModel(store)
        advanceUntilIdle()
        assertTrue(vm2.hasUserDarkThemeEnabled)
    }
}
