package com.mukund.bookcompanion.design


import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.mukund.bookcompanion.R

// ─── Font Families ──────────────────────────────────────────
/**
 * Cormorant Garamond — serif typeface for display, titles, and serif body text
 * Weights: 400 (Regular), 400i (Italic), 500 (Medium), 500i (Medium Italic)
 * Downloaded from: https://fonts.google.com/specimen/Cormorant+Garamond
 */
val CormorantGaramond = FontFamily(
    Font(R.font.cormorant_garamond, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.cormorant_garamond_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.cormorant_garamond_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.cormorant_garamond_medium_italic, FontWeight.Medium, FontStyle.Italic),
)

/**
 * IBM Plex Sans — clean sans-serif for UI, labels, and metadata
 * Weights: 400 (Regular), 500 (Medium)
 * Downloaded from: https://fonts.google.com/specimen/IBM+Plex+Sans
 */
val IBMPlexSans = FontFamily(
    Font(R.font.ibm_plex_sans, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_medium, FontWeight.Medium),
)

/**
 * JetBrains Mono — monospace font for technical metadata and counts
 * Weight: 400 (Regular)
 * Downloaded from: https://fonts.google.com/specimen/JetBrains+Mono
 */
val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono, FontWeight.Normal),
)

// ─── Typography Styles (for use in Text composables) ────────
/**
 * Display style — large serif headline (app title, major sections)
 * Example: "Book Companion" title on home screen
 */
object DisplayTypography {
    val fontSize = BookCompanionTypography.display.fontSize
    val lineHeight = BookCompanionTypography.display.lineHeight
    val fontFamily = CormorantGaramond
    val fontWeight = FontWeight.Medium
    val letterSpacing = BookCompanionTypography.display.letterSpacing
}

/**
 * Heading Large — serif subtitle/section title (26sp)
 * Example: Book title in list item
 */
object HeadingLargeTypography {
    val fontSize = BookCompanionTypography.headingLarge.fontSize
    val lineHeight = BookCompanionTypography.headingLarge.lineHeight
    val fontFamily = CormorantGaramond
    val fontWeight = FontWeight.Medium
    val letterSpacing = BookCompanionTypography.headingLarge.letterSpacing
}

/**
 * Body Large — serif body text with italic emphasis (17sp)
 * Example: Book author, descriptive text
 */
object BodyLargeTypography {
    val fontSize = BookCompanionTypography.bodyLarge.fontSize
    val lineHeight = BookCompanionTypography.bodyLarge.lineHeight
    val fontFamily = CormorantGaramond
    val fontWeight = FontWeight.Normal
    val fontStyle = FontStyle.Italic
}

/**
 * Body — standard sans-serif text (15sp)
 * Example: Book metadata, UI text
 */
object BodyTypography {
    val fontSize = BookCompanionTypography.body.fontSize
    val lineHeight = BookCompanionTypography.body.lineHeight
    val fontFamily = IBMPlexSans
    val fontWeight = FontWeight.Normal
    val letterSpacing = BookCompanionTypography.body.letterSpacing
}

/**
 * Body Small — smaller sans-serif text (13sp)
 * Example: Secondary metadata, captions
 */
object BodySmallTypography {
    val fontSize = BookCompanionTypography.bodySmall.fontSize
    val lineHeight = BookCompanionTypography.bodySmall.lineHeight
    val fontFamily = IBMPlexSans
    val fontWeight = FontWeight.Normal
    val letterSpacing = BookCompanionTypography.bodySmall.letterSpacing
}

/**
 * Label Small — emphasized sans-serif label (12sp)
 * Example: Filter tabs, button text
 */
object LabelSmallTypography {
    val fontSize = BookCompanionTypography.labelSmall.fontSize
    val lineHeight = BookCompanionTypography.labelSmall.lineHeight
    val fontFamily = IBMPlexSans
    val fontWeight = FontWeight.Medium
    val letterSpacing = BookCompanionTypography.labelSmall.letterSpacing
}

/**
 * Label Tiny — uppercase monospace label (10sp)
 * Example: "Genre · Status", "Read", year badges
 */
object LabelTinyTypography {
    val fontSize = BookCompanionTypography.labelTiny.fontSize
    val lineHeight = BookCompanionTypography.labelTiny.lineHeight
    val fontFamily = JetBrainsMono
    val fontWeight = FontWeight.Normal
    val letterSpacing = BookCompanionTypography.labelTiny.letterSpacing
}

/**
 * Caption — small sans-serif caption text (11sp)
 * Example: Metadata line under book title
 */
object CaptionTypography {
    val fontSize = BookCompanionTypography.caption.fontSize
    val lineHeight = BookCompanionTypography.caption.lineHeight
    val fontFamily = IBMPlexSans
    val fontWeight = FontWeight.Normal
    val letterSpacing = BookCompanionTypography.caption.letterSpacing
}