package com.zavedahmad.yaHabit

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

val navigationForwardTransitionSpec : AnimatedContentTransitionScope<*>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { it / 4 }, animationSpec = tween(
            durationMillis = 200,
            easing = CubicBezierEasing(0f, 1f, 0.6f, 1f)
        )
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = 100,

            easing = CubicBezierEasing(0f, 1f, 0.6f, 1f)
        )
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { -it / 2 }, animationSpec = tween(
            durationMillis = 200,
            easing = CubicBezierEasing(0f, 1f, 0.6f, 1f)
        )
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = 100,

            easing = CubicBezierEasing(0f, 1f, 0.6f, 1f)
        )
    )
}
val predictiveTransition: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { -it / 2 }, animationSpec = tween(
            durationMillis = 200,
            easing = CubicBezierEasing(0f, 1f, 0.6f, 1f)
        )
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = 100,

            easing = CubicBezierEasing(0f, 0f, 1f, 0.6f)
        )
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { it / 4 }, animationSpec = tween(
            durationMillis = 200,
            easing = CubicBezierEasing(0f, 1f, 0.6f, 1f)
        )
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = 100,

            easing = CubicBezierEasing(0f, 1f, 0.6f, 1f)
        )
    )
}