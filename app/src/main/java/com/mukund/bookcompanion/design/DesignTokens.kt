package com.mukund.bookcompanion.design

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

// ─── Material3 bridge ───────────────────────────────────────
/**
 * Material3 [ColorScheme]s derived from the editorial palette so that anything that falls back to
 * `MaterialTheme.colorScheme` (ripples, text-selection handles, component defaults, the validation
 * `error` color) stays on-brand instead of pulling wallpaper-derived dynamic colors.
 */
fun editorialLightColorScheme(): ColorScheme = LightColorScheme.toMaterialColorScheme(dark = false)
fun editorialDarkColorScheme(): ColorScheme = DarkColorScheme.toMaterialColorScheme(dark = true)

private fun BookCompanionColorScheme.toMaterialColorScheme(dark: Boolean): ColorScheme {
    val base = if (dark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary            = ink,
        onPrimary          = paper,
        primaryContainer   = paperDeep,
        onPrimaryContainer = ink,
        secondary          = sage,
        onSecondary        = paper,
        tertiary           = ochre,
        onTertiary         = paper,
        background         = paper,
        onBackground       = ink,
        surface            = paper,
        onSurface          = ink,
        surfaceVariant     = paperDeep,
        onSurfaceVariant   = inkSoft,
        surfaceContainer       = paperDeep,
        surfaceContainerLow    = paper,
        surfaceContainerHigh   = paperDeep,
        outline            = rule,
        outlineVariant     = ruleSoft,
        error              = terracotta,
        onError            = paper,
        errorContainer     = terracotta.copy(alpha = 0.12f),
        onErrorContainer   = terracotta,
    )
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

    // Screen gutter — the standard horizontal page inset used by every screen/header/card.
    val gutter: Dp = 28.dp

    // Status indicator dot diameter (Unread/Reading/Read).
    val statusDot: Dp = 7.dp

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
// Intentionally omitted: the editorial design uses hairline borders (see BookCompanionBorders),
// not drop shadows. The previous CSS-string "elevation" tokens were copied from a web prototype
// and could never be consumed by Compose, so they were removed rather than left as dead code.

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
