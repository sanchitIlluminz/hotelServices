package com.core.utils

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateTimeUtils {
    private lateinit var FORMATTER_WEEKDAY_WITH_TIME: DateTimeFormatter
    private lateinit var FORMATTER_DATE: DateTimeFormatter
    private lateinit var FORMATTER_DATE_AVAILABILITY: DateTimeFormatter
    private lateinit var FORMATTER_MONTH_DAY: DateTimeFormatter
    private lateinit var FORMATTER_DATE_TIME: DateTimeFormatter

    init {
        recreateFormatter()
    }

    private fun recreateFormatter() {
        FORMATTER_DATE_TIME = DateTimeFormatter.ofPattern("dd MMM · hh:mm a")
        FORMATTER_WEEKDAY_WITH_TIME = DateTimeFormatter.ofPattern("EEE · hh:mm a")
        FORMATTER_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy")
        FORMATTER_DATE_AVAILABILITY = DateTimeFormatter.ofPattern("dd MMM,yyyy")
        FORMATTER_MONTH_DAY = DateTimeFormatter.ofPattern("MMM dd")
    }

    fun format(zonedDateTime: ZonedDateTime?, formatter: DateTimeFormatter?): String {
        zonedDateTime ?: return ""
        formatter ?: return ""
        return zonedDateTime.format(formatter)
    }

    fun formatWeekdayWithTime(zonedDateTime: ZonedDateTime): String {
        return format(zonedDateTime, FORMATTER_WEEKDAY_WITH_TIME)
    }

    fun formatCompleteDateTime(zonedDateTime: ZonedDateTime?): String {
        return format(zonedDateTime, FORMATTER_DATE_TIME)
    }
}