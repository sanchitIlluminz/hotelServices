package com.core.extensions

private const val ZERO = '0'

fun String.isAllWhitespace(): Boolean {
    return indices.all { this[it].isWhitespace() }
}

fun String.isAllZeroes(): Boolean {
    return indices.all { this[it] == ZERO }
}

fun getUTCHours(offsetInMinutes: Int?): String? {

    var str = ""
    val hours: Int = offsetInMinutes?.div(60).orZero()
    val minutes: Int = offsetInMinutes?.rem(60).orZero()
    str = when {
        offsetInMinutes.orZero() > 0 -> "UTC +" + String.format("%02d:%02d", hours, minutes)
        offsetInMinutes.orZero() < 0 -> "UTC " + String.format("%02d:%02d", hours, minutes)
        else -> "UTC"
    }
    return str
}