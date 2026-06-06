package com.mukund.bookcompanion.navigation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeResolutionTest {

    @Test
    fun resolveTheme_systemModeOn_usesSystemDarkValue_true() {
        assertTrue(resolveTheme(followSystem = true, userDark = false, isSystemDark = true))
    }

    @Test
    fun resolveTheme_systemModeOn_usesSystemDarkValue_false() {
        assertFalse(resolveTheme(followSystem = true, userDark = true, isSystemDark = false))
    }

    @Test
    fun resolveTheme_systemModeOff_usesUserPreference_true() {
        assertTrue(resolveTheme(followSystem = false, userDark = true, isSystemDark = false))
    }

    @Test
    fun resolveTheme_systemModeOff_usesUserPreference_false() {
        assertFalse(resolveTheme(followSystem = false, userDark = false, isSystemDark = true))
    }
}
