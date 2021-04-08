package com.core.extensions

fun Int?.toBoolean(): Boolean {
    return this == 1
}

fun Boolean?.toInt(): Int {
    return if (this == true) 1 else 0
}

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0L

fun Float?.orZero(): Float = this ?: 0.0f

fun Double?.orZero(): Double = this ?: 0.0

fun Double?.isNullOrZero(): Boolean = this == null || this == 0.0

fun Boolean?.orFalse(): Boolean = this ?: false