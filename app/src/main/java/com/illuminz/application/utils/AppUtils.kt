package com.illuminz.application.utils

import android.content.Context
import com.core.extensions.openUrl
import java.text.DecimalFormat

object AppUtils {
    private const val USER_APP_STORE_LINK =
        "https://play.google.com/store/apps/details?id=com.gigipo"


    fun roundDecimalValue(amount: Double, maxDecimalPlaces: Int = 2): String {
        return if (amount > 0) {
            val df = DecimalFormat("#.##")
            df.maximumFractionDigits = maxDecimalPlaces
            df.format(amount)
            // BigDecimal(amount).setScale(maxDecimalPlaces, RoundingMode.HALF_EVEN).toDouble()
        } else {
            "0.0"
        }
    }

    fun roundDecimalValueAndTruncateDecimalPlaces(
        amount: Double,
        maxDecimalPlaces: Int = 2
    ): String {
        return if (amount > 0) {
            val df = DecimalFormat("###.#")
            return df.format(amount)
        } else {
            "0"
        }
    }

    fun openUserAppStoreLink(context: Context) {
        context.openUrl(USER_APP_STORE_LINK)
    }
}