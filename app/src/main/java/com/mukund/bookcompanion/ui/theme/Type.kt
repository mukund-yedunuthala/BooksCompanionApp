package com.mukund.bookcompanion.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.design.CormorantGaramond
import com.mukund.bookcompanion.design.IBMPlexSans
import com.mukund.bookcompanion.design.JetBrainsMono

/**
 * App typography — a single source of truth for all text styles.
 *
 * Color is deliberately excluded from every style; pass `color = bookColors.xxx` at
 * each call site so theme-reactive colors continue to work correctly.
 *
 * Usage:
 *   Text("Book", style = AppType.displaySerifItalic, color = bookColors.ink)
 */
object AppType {
    // ── Serif display ─────────────────────────────────────────
    /** 52 sp · Medium · Italic  — screen wordmarks ("Book", "Settings", "Backup & Restore") */
    val displaySerifItalic = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontStyle  = FontStyle.Italic,
        fontSize   = 52.sp,
        lineHeight  = 48.sp,
        letterSpacing = (-0.02).sp,
    )

    /** 52 sp · Normal · Normal — second wordmark line ("Companion") */
    val displaySerif = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Normal,
        fontStyle  = FontStyle.Normal,
        fontSize   = 52.sp,
        lineHeight  = 48.sp,
        letterSpacing = (-0.02).sp,
    )

    // ── Serif headings ────────────────────────────────────────
    /** 30 sp · Medium — Overview book title */
    val titleSerifLarge = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontSize   = 30.sp,
        lineHeight  = 33.sp,
        letterSpacing = (-0.01).sp,
    )

    /** 26 sp · Medium — list card book title / bottom-sheet heading */
    val titleSerif = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontSize   = 26.sp,
        lineHeight  = 27.sp,
        letterSpacing = (-0.01).sp,
    )

    /** 22 sp · Medium — progress counter numerals */
    val headingSerif = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontSize   = 22.sp,
    )

    /** 22 sp · Normal — progress counter "of" / "read" connector */
    val headingSerifLight = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Normal,
        fontSize   = 22.sp,
    )

    // ── Serif body / author ───────────────────────────────────
    /** 17 sp · Italic — Overview author ("by …") */
    val authorSerifItalicLarge = TextStyle(
        fontFamily = CormorantGaramond,
        fontStyle  = FontStyle.Italic,
        fontSize   = 17.sp,
    )

    /** 15 sp · Italic — card author ("by …") */
    val authorSerifItalic = TextStyle(
        fontFamily = CormorantGaramond,
        fontStyle  = FontStyle.Italic,
        fontSize   = 15.sp,
    )

    // ── Bottom-sheet action label ─────────────────────────────
    /** 16 sp · Medium · Italic — "Add to library →" / "Save changes →" */
    val sheetActionSerif = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontStyle  = FontStyle.Italic,
        fontSize   = 16.sp,
    )

    /** 14 sp · Medium · Italic — FAB "New entry" label */
    val fabLabelSerif = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontStyle  = FontStyle.Italic,
        fontSize   = 14.sp,
        letterSpacing = 0.01.sp,
    )

    // ── Sans-serif UI text ────────────────────────────────────
    /** 15 sp — standard body prose */
    val body = TextStyle(
        fontFamily = IBMPlexSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 15.sp,
        letterSpacing = 0.02.sp,
    )

    /** 13 sp — secondary UI text, back-nav labels, captions */
    val bodySmall = TextStyle(
        fontFamily = IBMPlexSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 13.sp,
        lineHeight  = 20.sp,
        letterSpacing = 0.02.sp,
    )

    /** 12 sp · Medium — filter-tab active labels */
    val labelSmall = TextStyle(
        fontFamily = IBMPlexSans,
        fontWeight = FontWeight.Medium,
        fontSize   = 12.sp,
        letterSpacing = 0.04.sp,
    )

    /** 12 sp · Normal — filter-tab inactive / validation messages */
    val labelSmallLight = TextStyle(
        fontFamily = IBMPlexSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        letterSpacing = 0.04.sp,
    )

    /** 11 sp — metadata captions (year, pages) */
    val caption = TextStyle(
        fontFamily = IBMPlexSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 11.sp,
        letterSpacing = 0.02.sp,
    )

    // ── Monospace metadata ────────────────────────────────────
    /** 10 sp — date bar, small mono labels */
    val labelTinyMono = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize   = 10.sp,
        letterSpacing = 0.12.sp,
    )

    /** 9 sp — genre, status, section headers, filter tab count badges (most common) */
    val labelMicroMono = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize   = 9.sp,
        letterSpacing = 0.14.sp,
    )
}

// Material3 typography — keep bodyLarge wired up; the rest of our UI uses AppType directly
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = IBMPlexSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 15.sp,
        lineHeight  = 22.sp,
        letterSpacing = 0.02.sp,
    )
)
