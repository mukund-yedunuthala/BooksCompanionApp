package com.mukund.bookcompanion.design

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.mukund.bookcompanion.R

// ─── Font Families ──────────────────────────────────────────
/**
 * Cormorant Garamond — serif typeface for display, titles, and body text.
 * Weights: 400 Regular / 400 Italic / 500 Medium / 500 Medium Italic
 * https://fonts.google.com/specimen/Cormorant+Garamond
 */
val CormorantGaramond = FontFamily(
    Font(R.font.cormorant_garamond, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.cormorant_garamond_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.cormorant_garamond_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.cormorant_garamond_medium_italic, FontWeight.Medium, FontStyle.Italic),
)

/**
 * IBM Plex Sans — clean sans-serif for UI and labels.
 * Weights: 400 Regular / 500 Medium
 * https://fonts.google.com/specimen/IBM+Plex+Sans
 */
val IBMPlexSans = FontFamily(
    Font(R.font.ibm_plex_sans, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_medium, FontWeight.Medium),
)

/**
 * JetBrains Mono — monospace for metadata, counts, and labels.
 * Weight: 400 Regular
 * https://fonts.google.com/specimen/JetBrains+Mono
 */
val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono, FontWeight.Normal),
)

// ─── Compose TextStyle constants live in ui/theme/Type.kt (AppType object) ────