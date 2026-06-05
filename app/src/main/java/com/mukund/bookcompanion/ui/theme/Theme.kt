package com.mukund.bookcompanion.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.mukund.bookcompanion.design.BookCompanionColorScheme
import com.mukund.bookcompanion.design.DarkColorScheme
import com.mukund.bookcompanion.design.LightColorScheme
import com.mukund.bookcompanion.design.LocalBookCompanionColors

@Composable
fun BooksCompanionTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = if (darkTheme) dynamicDarkColorScheme(context)
                      else           dynamicLightColorScheme(context)

    val bookColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

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