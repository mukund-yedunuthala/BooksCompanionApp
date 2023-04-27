package com.mukund.bookcompanion.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

fun customPopEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
    return {
        slideInHorizontally(
            initialOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeIn(animationSpec = tween(300))
    }
}
fun customEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
    return {
        slideInHorizontally(
            initialOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeIn(animationSpec = tween(300))
    }
}
fun customPopExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
    return {
        slideOutHorizontally(
            targetOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeOut(animationSpec = tween(300))
    }
}
fun customExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
    return {
        slideOutHorizontally(
            targetOffsetX = { 300 },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) +
                fadeOut(animationSpec = tween(300))
    }
}