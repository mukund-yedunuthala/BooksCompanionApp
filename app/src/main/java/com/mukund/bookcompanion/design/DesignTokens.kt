package com.mukund.bookcompanion.design

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Color Palette ──────────────────────────────────────────
/**
 * Warm editorial color palette inspired by Monocle / Kinfolk aesthetic.
 * All colors work in both light and dark mode.
 */
object BookCompanionColors {
    // ── Light palette ─────────────────────────────────────────
    val paper        = Color(0xFFF2EADB)
    val paperDeep    = Color(0xFFEBE1CE)
    val ink          = Color(0xFF1C1814)
    val inkSoft      = Color(0xFF4A3F36)
    val inkFaint     = Color(0xFF8B7E6F)
    val rule         = Color(0xFFD8CBB2)
    val ruleSoft     = Color(0xFFE5DCC7)
    val terracotta   = Color(0xFFB8543E)
    val sage         = Color(0xFF6B7F5A)
    val ochre        = Color(0xFFB8893E)

    // ── Dark palette ──────────────────────────────────────────
    val darkPaper      = Color(0xFF1A1410)
    val darkPaperDeep  = Color(0xFF2A2420)
    val darkInk        = Color(0xFFE8DCC8)
    val darkInkSoft    = Color(0xFFB8A894)
    val darkInkFaint   = Color(0xFF7A6F64)
    val darkRule       = Color(0xFF3D3530)
    val darkRuleSoft   = Color(0xFF2E2924)

    // Semantic accents are shared — terracotta, sage, ochre
    // read well on both light and dark backgrounds
}

// Resolved color set for the current theme mode
data class BookCompanionColorScheme(
    val paper: Color,
    val paperDeep: Color,
    val ink: Color,
    val inkSoft: Color,
    val inkFaint: Color,
    val rule: Color,
    val ruleSoft: Color,
    val terracotta: Color,
    val sage: Color,
    val ochre: Color,
)

val LightColorScheme = BookCompanionColorScheme(
    paper      = BookCompanionColors.paper,
    paperDeep  = BookCompanionColors.paperDeep,
    ink        = BookCompanionColors.ink,
    inkSoft    = BookCompanionColors.inkSoft,
    inkFaint   = BookCompanionColors.inkFaint,
    rule       = BookCompanionColors.rule,
    ruleSoft   = BookCompanionColors.ruleSoft,
    terracotta = BookCompanionColors.terracotta,
    sage       = BookCompanionColors.sage,
    ochre      = BookCompanionColors.ochre,
)

val DarkColorScheme = BookCompanionColorScheme(
    paper      = BookCompanionColors.darkPaper,
    paperDeep  = BookCompanionColors.darkPaperDeep,
    ink        = BookCompanionColors.darkInk,
    inkSoft    = BookCompanionColors.darkInkSoft,
    inkFaint   = BookCompanionColors.darkInkFaint,
    rule       = BookCompanionColors.darkRule,
    ruleSoft   = BookCompanionColors.darkRuleSoft,
    terracotta = BookCompanionColors.terracotta,
    sage       = BookCompanionColors.sage,
    ochre      = BookCompanionColors.ochre,
)

// CompositionLocal so any composable can access the current scheme
val LocalBookCompanionColors = compositionLocalOf { LightColorScheme }
// ─── Typography ─────────────────────────────────────────────
/**
 * Typefaces for Book Companion.
 * Fonts must be downloaded from Google Fonts and placed in res/font/
 */
object BookCompanionFonts {
    // Font family names (match res/font filenames)
    const val SERIF_FAMILY = "cormorant_garamond"      // Google Fonts: Cormorant Garamond
    const val SANS_FAMILY = "ibm_plex_sans"            // Google Fonts: IBM Plex Sans
    const val MONO_FAMILY = "jetbrains_mono"           // Google Fonts: JetBrains Mono
}

/**
 * Typographic scale and styles for Book Companion.
 * Uses serif for display/headers, sans for UI, mono for metadata.
 */
object BookCompanionTypography {
    // Display & Headers
    data class DisplayStyle(
        val fontSize: TextUnit = 52.sp,
        val lineHeight: TextUnit = 48.sp,
        val fontFamily: String = BookCompanionFonts.SERIF_FAMILY,
        val fontWeight: Int = 500,  // medium
        val letterSpacing: TextUnit = (-0.02).sp,
    )

    data class HeadingLargeStyle(
        val fontSize: TextUnit = 26.sp,
        val lineHeight: TextUnit = 27.sp,
        val fontFamily: String = BookCompanionFonts.SERIF_FAMILY,
        val fontWeight: Int = 500,  // medium
        val letterSpacing: TextUnit = (-0.01).sp,
    )

    data class HeadingMediumStyle(
        val fontSize: TextUnit = 22.sp,
        val lineHeight: TextUnit = 20.sp,
        val fontFamily: String = BookCompanionFonts.SERIF_FAMILY,
        val fontWeight: Int = 500,  // medium
        val letterSpacing: TextUnit = 0.sp,
    )

    data class BodyLargeStyle(
        val fontSize: TextUnit = 17.sp,
        val lineHeight: TextUnit = 26.sp,
        val fontFamily: String = BookCompanionFonts.SERIF_FAMILY,
        val fontWeight: Int = 400,  // normal
        val letterSpacing: TextUnit = 0.sp,
        val fontStyle: String = "italic",  // body serif text is italicized
    )

    data class BodyStyle(
        val fontSize: TextUnit = 15.sp,
        val lineHeight: TextUnit = 22.sp,
        val fontFamily: String = BookCompanionFonts.SANS_FAMILY,
        val fontWeight: Int = 400,  // normal
        val letterSpacing: TextUnit = 0.02.sp,
    )

    data class BodySmallStyle(
        val fontSize: TextUnit = 13.sp,
        val lineHeight: TextUnit = 20.sp,
        val fontFamily: String = BookCompanionFonts.SANS_FAMILY,
        val fontWeight: Int = 400,  // normal
        val letterSpacing: TextUnit = 0.02.sp,
    )

    data class LabelSmallStyle(
        val fontSize: TextUnit = 12.sp,
        val lineHeight: TextUnit = 16.sp,
        val fontFamily: String = BookCompanionFonts.SANS_FAMILY,
        val fontWeight: Int = 500,  // medium
        val letterSpacing: TextUnit = 0.04.sp,
    )

    data class LabelTinyStyle(
        val fontSize: TextUnit = 10.sp,
        val lineHeight: TextUnit = 14.sp,
        val fontFamily: String = BookCompanionFonts.MONO_FAMILY,
        val fontWeight: Int = 400,  // normal
        val letterSpacing: TextUnit = 0.14.sp,
        val textTransform: String = "uppercase",
    )

    data class CaptionStyle(
        val fontSize: TextUnit = 11.sp,
        val lineHeight: TextUnit = 16.sp,
        val fontFamily: String = BookCompanionFonts.SANS_FAMILY,
        val fontWeight: Int = 400,  // normal
        val letterSpacing: TextUnit = 0.02.sp,
    )

    // Pre-instantiated style objects
    val display = DisplayStyle()
    val headingLarge = HeadingLargeStyle()
    val headingMedium = HeadingMediumStyle()
    val bodyLarge = BodyLargeStyle()
    val body = BodyStyle()
    val bodySmall = BodySmallStyle()
    val labelSmall = LabelSmallStyle()
    val labelTiny = LabelTinyStyle()
    val caption = CaptionStyle()
}

// ─── Spacing ────────────────────────────────────────────────
/**
 * Spacing scale for consistent rhythm.
 * Uses 8dp base unit with 4dp half-step increments for flexibility.
 */
object BookCompanionSpacing {
    // Base unit
    val xs: Dp = 4.dp          // 4dp — extra small gaps, internal padding
    val sm: Dp = 8.dp          // 8dp — small gaps between elements
    val md: Dp = 12.dp         // 12dp — default gap
    val lg: Dp = 16.dp         // 16dp — larger gaps
    val xl: Dp = 24.dp         // 24dp — section separator
    val xxl: Dp = 32.dp        // 32dp — major section padding

    // Padding presets (for cards, screens)
    val paddingSmall: Dp = 12.dp
    val paddingMedium: Dp = 16.dp
    val paddingLarge: Dp = 24.dp
    val paddingXLarge: Dp = 28.dp

    // Margin presets
    val marginSmall: Dp = 8.dp
    val marginMedium: Dp = 12.dp
    val marginLarge: Dp = 16.dp
    val marginXLarge: Dp = 24.dp

    // Vertical rhythm (for section spacing)
    val verticalSmall: Dp = 12.dp
    val verticalMedium: Dp = 16.dp
    val verticalLarge: Dp = 24.dp
    val verticalXLarge: Dp = 32.dp

    // Component-specific
    val buttonPaddingVertical: Dp = 10.dp
    val buttonPaddingHorizontal: Dp = 16.dp
    val chipPaddingVertical: Dp = 6.dp
    val chipPaddingHorizontal: Dp = 12.dp
}

// ─── Corner Radius ──────────────────────────────────────────
/**
 * Border radius scale for consistent shape language.
 */
object BookCompanionRadius {
    val small: Dp = 4.dp        // hairline curves on small elements
    val medium: Dp = 8.dp       // default — most UI components
    val large: Dp = 12.dp       // cards and larger surfaces
    val xlarge: Dp = 20.dp      // bottom sheet, modals
    val pill: Dp = 999.dp       // fully rounded buttons/badges
}

// ─── Elevation & Shadows ────────────────────────────────────
/**
 * Shadows for depth and emphasis (editorial aesthetic — used sparingly).
 */
object BookCompanionElevation {
    /**
     * Subtle card shadow — used for elevated surfaces like bottom sheet.
     */
    val cardShadow = "0 4px 14px rgba(28, 24, 20, 0.18)"

    /**
     * Deep modal shadow — used for backdrop and modal elevation.
     */
    val modalShadow = "0 20px 60px rgba(28, 24, 20, 0.15)"

    /**
     * Button press shadow — when button is activated.
     */
    val pressedShadow = "0 2px 8px rgba(28, 24, 20, 0.12)"
}

// ─── Border Widths ──────────────────────────────────────────
/**
 * Hairline borders for the editorial aesthetic.
 */
object BookCompanionBorders {
    val hairline: Dp = 0.5.dp   // default — most borders and dividers
    val emphasis: Dp = 1.dp     // stronger accent borders
    val thick: Dp = 2.dp        // featured/highlighted borders (e.g., active filter)
}

// ─── Opacity ────────────────────────────────────────────────
/**
 * Opacity scale for hierarchy and subtle states.
 */
object BookCompanionOpacity {
    val full: Float = 1.0f
    val strong: Float = 0.87f
    val medium: Float = 0.60f
    val soft: Float = 0.38f
    val faint: Float = 0.12f
    val disabled: Float = 0.38f
}

// ─── Animation Durations ────────────────────────────────────
/**
 * Timing for transitions and micro-interactions.
 */
object BookCompanionTiming {
    val fast: Long = 200          // ms — quick micro-interactions (button hover, focus)
    val medium: Long = 300        // ms — standard transitions (color change, fade)
    val slow: Long = 400          // ms — deliberate animations (sheet slide, list expand)
    val slower: Long = 500        // ms — emphasis animations (page transitions)
}
