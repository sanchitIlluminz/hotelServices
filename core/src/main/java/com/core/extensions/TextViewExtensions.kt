package com.core.extensions

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.core.R
import com.core.utils.CustomTypefaceSpan
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


fun TextView.foregroundSpannable(
    spannedText: String, @ColorRes textColorRes: Int,
    textTypeface: Typeface? = null, textSize: Float? = null
) {
    val spannable: Spannable = SpannableString.valueOf(text)

    val startIndex = text.indexOf(spannedText)
    val endIndex = startIndex + spannedText.length
    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, textColorRes)),
        startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    if (textTypeface != null) {
        spannable.setSpan(
            CustomTypefaceSpan(textTypeface),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    if (textSize != null) {
        spannable.setSpan(
            RelativeSizeSpan(textSize),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    this.setText(spannable, TextView.BufferType.SPANNABLE)
}

fun TextView.foregroundSpannable(
    startIndex: Int,
    endIndex: Int,
    @ColorRes textColorRes: Int,
    textTypeface: Typeface? = null,
    textSize: Float? = null
) {
    val spannable: Spannable = SpannableString.valueOf(text)

    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, textColorRes)),
        startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    if (textTypeface != null) {
        spannable.setSpan(
            CustomTypefaceSpan(textTypeface),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    if (textSize != null) {
        spannable.setSpan(
            RelativeSizeSpan(textSize),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    this.setText(spannable, TextView.BufferType.SPANNABLE)
}

fun TextView.clickSpannable(
    spannableText: String, @ColorRes textColorRes: Int,
    textTypeface: Typeface?,
    underlineText: Boolean = false,
    clickListener: View.OnClickListener
) {
    val fullText = text.toString()
    val startIndex = fullText.indexOf(spannableText)
    val spannable: Spannable = SpannableString.valueOf(text)

    val clickable = object : ClickableSpan() {
        override fun updateDrawState(textPaint: TextPaint) {
            super.updateDrawState(textPaint)
            textPaint.isUnderlineText = underlineText
            textPaint.color = ContextCompat.getColor(context, textColorRes)
            if (textTypeface != null) {
                textPaint.typeface = textTypeface
            }
        }

        override fun onClick(widget: View) {
            clickListener.onClick(widget)
        }
    }
    spannable.setSpan(
        clickable,
        startIndex,
        startIndex + spannableText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
    setText(spannable, TextView.BufferType.SPANNABLE)
}

fun generateFollowerCount(numberOfFollowers: Int?): CharSequence? {
    return when {
        abs(numberOfFollowers?.div(1000000).orZero()) > 1 -> {
            (numberOfFollowers?.div(1000000).orZero()).toString() + "M"
        }
        abs(numberOfFollowers?.div(1000).orZero()) > 1 -> {
            (numberOfFollowers?.div(1000).orZero()).toString() + "K"
        }
        else -> {
            numberOfFollowers.toString()
        }
    }
}


fun Context.getRelativeTimeSpanString(time: Long): String? {
    val dateStr = StringBuffer()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    val now = Calendar.getInstance()
    val days: Int = daysBetween(calendar.time, now.time)
    val weeks = days / 7
    val minutes: Int = hoursBetween(calendar.time, now.time)
    val hours = minutes / 60
    if (days == 0) {
        val second: Int =
            minuteBetween(calendar.time, now.time)
        if (minutes > 60) {
            if (hours in 1..24) {
                dateStr.append(
                    String.format(
                        "%s %s %s",
                        hours,
                        getString(R.string.hour), getString(R.string.ago)
                    )
                )
            }
        } else {
            if (second <= 10) {
                dateStr.append(getString(R.string.just_now))
            } /*else if (second > 10 && second <= 30) {
                    dateStr.append(context.getString(R.string.few_seconds_ago));
                }*/ else if (second > 10 && second <= 60) {
                dateStr.append(
                    String.format(
                        "%s %s %s",
                        second,
                        getString(R.string.seconds), getString(R.string.ago)
                    )
                )
            } else if (second >= 60 && minutes <= 60) {
                dateStr.append(
                    String.format(
                        "%s %s %s",
                        minutes,
                        getString(R.string.minutes), getString(R.string.ago)
                    )
                )
            }
        }
    } else if (hours > 24 && days < 7) {
        dateStr.append(
            String.format(
                "%s %s %s",
                days,
                getString(R.string.days), getString(R.string.ago)
            )
        )
    } else if (weeks == 1) {
        dateStr.append(
            String.format(
                "%s %s %s",
                weeks,
                getString(R.string.weeks), getString(R.string.ago)
            )
        )
    } else {
        /**
         * make formatted createTime by languageTag("fa")
         *
         * @return
         */
        return SimpleDateFormat.getDateInstance(
            SimpleDateFormat.LONG,
            Locale("EN")
        )
            .format(calendar.time).toUpperCase(Locale.ROOT)
    }
    return dateStr.toString()
}

fun minuteBetween(d1: Date, d2: Date): Int {
    return ((d2.time - d1.time) / DateUtils.SECOND_IN_MILLIS).toInt()
}

fun hoursBetween(d1: Date, d2: Date): Int {
    return ((d2.time - d1.time) / DateUtils.MINUTE_IN_MILLIS).toInt()
}

fun daysBetween(d1: Date, d2: Date): Int {
    return ((d2.time - d1.time) / DateUtils.DAY_IN_MILLIS).toInt()
}

fun generate(phrase: String): String = phrase.split(" ", "-")
    .mapNotNull { word -> word.firstOrNull() }
    .joinToString("")
    .toUpperCase(Locale.ROOT)