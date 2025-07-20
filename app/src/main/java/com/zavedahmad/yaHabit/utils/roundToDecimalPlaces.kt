package com.zavedahmad.yaHabit.utils

import kotlin.math.pow
import kotlin.math.roundToLong

fun Double.roundTo(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return (this * factor).roundToLong() / factor
}

