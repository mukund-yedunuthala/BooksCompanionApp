package com.mukund.bookcompanion.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.mukund.bookcompanion.design.BookCompanionColorScheme
import com.mukund.bookcompanion.design.DarkColorScheme
import com.mukund.bookcompanion.design.LightColorScheme
import com.mukund.bookcompanion.design.LocalBookCompanionColors
import com.mukund.bookcompanion.design.editorialDarkColorScheme
import com.mukund.bookcompanion.design.editorialLightColorScheme

@Composable
fun BooksCompanionTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) editorialDarkColorScheme() else editorialLightColorScheme()
    val bookColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Keep system-bar icon contrast in sync with the *in-app* theme, which can diverge from the
    // system theme. Without this, dark icons can land on a dark paper background (or vice versa).
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalBookCompanionColors provides bookColorScheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val bookColors: BookCompanionColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalBookCompanionColors.current