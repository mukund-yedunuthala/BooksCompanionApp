package com.mukund.bookcompanion.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import com.mukund.bookcompanion.design.BookCompanionTiming

/**
 * Shared screen transitions, defined once so navigating between any two screens
 * animates identically. These are wired as NavHost-level defaults in NavGraph —
 * do not re-declare them per destination.
 *
 * Motion model (mirrored push/pop):
 *  - Forward: the new screen slides in from the right while the old one slides out to the left.
 *  - Back:    the top screen slides out to the right while the revealed one slides in from the left.
 */

/** Horizontal slide distance as a fraction of screen width — subtle enough to keep the fade visible. */
private const val SlideDistanceDenominator = 4

/** Standard transition duration, from the design system timing token. */
private val transitionDurationMs = BookCompanionTiming.medium.toInt()

private fun slideSpec() = tween<IntOffset>(
    durationMillis = transitionDurationMs,
    easing = FastOutSlowInEasing
)

private fun fadeSpec() = tween<Float>(
    durationMillis = transitionDurationMs,
    easing = FastOutSlowInEasing
)

/** Forward navigation: this screen enters (slides in from the right). */
fun customEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth / SlideDistanceDenominator },
        animationSpec = slideSpec()
    ) + fadeIn(animationSpec = fadeSpec())
}

/** Forward navigation: this screen is covered (slides out to the left). */
fun customExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth / SlideDistanceDenominator },
        animationSpec = slideSpec()
    ) + fadeOut(animationSpec = fadeSpec())
}

/** Back navigation: this screen is revealed (slides in from the left). */
fun customPopEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth / SlideDistanceDenominator },
        animationSpec = slideSpec()
    ) + fadeIn(animationSpec = fadeSpec())
}

/** Back navigation: this screen is popped (slides out to the right). */
fun customPopExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth / SlideDistanceDenominator },
        animationSpec = slideSpec()
    ) + fadeOut(animationSpec = fadeSpec())
}
