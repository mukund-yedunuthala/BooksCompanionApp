package com.mukund.bookcompanion.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BooksCompanionTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val sdkVersion = Build.VERSION.SDK_INT
    val dynamicColor = sdkVersion >= Build.VERSION_CODES.R
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val bookColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalBookCompanionColors provides bookColorScheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme as ColorScheme,
            typography = Typography,
            content = content
        )
    }
}

val bookColors: BookCompanionColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalBookCompanionColors.current