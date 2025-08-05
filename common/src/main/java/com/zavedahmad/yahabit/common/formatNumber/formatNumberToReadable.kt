package com.zavedahmad.yahabit.common.formatNumber

fun formatNumberToReadable(number: Double) : String{
    return when {
        number >= 1_000_000_000_00 -> {
            String.format("%.1fT", number / 1_000_000_000_000.0)
        }
        number >= 100000000 -> {
            String.format("%.1fB", number / 1_000_000_000.0)
        }
        number >= 9500000 -> {
            String.format("%.0fM", number / 1_000_000.0)
        }
        number >= 950000 -> {
            String.format("%.1fM", number / 1_000_000.0)
        }
        number >= 9500-> {
            String.format("%.1fL", number / 100_000.0)
        }
        number >= 95 -> {
            String.format("%.1fK", number / 1_000.0)
        }
        else -> {
            if (number % 1 == 0.0) {
                String.format("%.0f", number)
            } else {
                String.format("%.1f", number)
            }
        }
    }
}