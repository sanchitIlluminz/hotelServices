package com.core.extensions

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.IsoFields

fun ZonedDateTime.isSameDay(other: ZonedDateTime): Boolean {
    return year == other.year && dayOfYear == other.dayOfYear
}

fun ZonedDateTime.isYesterday(other: ZonedDateTime): Boolean {
    return plusDays(1).isSameDay(other)
}

fun ZonedDateTime.isTomorrow(other: ZonedDateTime): Boolean {
    return minusDays(1).isSameDay(other)
}

fun ZonedDateTime.isSameWeek(other: ZonedDateTime): Boolean {
    return year == other.year && get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == other.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
}

fun ZonedDateTime.isSameMonth(other: ZonedDateTime): Boolean {
    return isSameYear(other) && monthValue == other.monthValue
}

fun ZonedDateTime.isSameYear(other: ZonedDateTime): Boolean {
    return year == other.year
}