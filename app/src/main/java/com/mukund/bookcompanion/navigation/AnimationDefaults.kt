package com.mukund.bookcompanion.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun customPopEnterTransition(): AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition? {
    return {
        slideInHorizontally(
            initialOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeIn(animationSpec = tween(300))
    }
}
@OptIn(ExperimentalAnimationApi::class)
fun customEnterTransition(): AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition? {
    return {
        slideInHorizontally(
            initialOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeIn(animationSpec = tween(300))
    }
}
@OptIn(ExperimentalAnimationApi::class)
fun customPopExitTransition(): AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition? {
    return {
        slideOutHorizontally(
            targetOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeOut(animationSpec = tween(300))
    }
}
@OptIn(ExperimentalAnimationApi::class)
fun customExitTransition(): AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition? {
    return {
        slideOutHorizontally(
            targetOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeOut(animationSpec = tween(300))
    }
}