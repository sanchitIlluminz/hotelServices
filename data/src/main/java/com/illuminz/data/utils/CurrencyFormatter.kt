package com.illuminz.data.utils

import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {
    private const val DEFAULT_LANGUAGE_CODE = "en_in"
    private const val DEFAULT_CURRENCY_CODE = "INR"
    private const val DEFAULT_COUNTRY_CODE = "IN"
    private const val DEFAULT_FRACTION_DIGITS = 2
    private const val DEFAULT_MIN_FRACTION_DIGITS = 0

//    private const val DECIMAL_FORMAT_PATTERN = "###,###,##0.00"
     private const val DECIMAL_FORMAT_PATTERN = "#0.00"

    private lateinit var selectedLanguageCode: String
    private lateinit var selectedLocale: Locale
    private lateinit var currency: Currency
    private lateinit var currencyFormatter: NumberFormat

    private val decimalFormatter: NumberFormat by lazy {
        DecimalFormat(DECIMAL_FORMAT_PATTERN)
    }

    init {
        setDefaultCurrencyAndLanguage()
    }

    fun getCurrencySymbol(currencyCode: String,
                          locale: Locale = selectedLocale): String {
        return try {
            val currency = Currency.getInstance(currencyCode)
            currency.getSymbol(locale)
        } catch (exception: Exception) {
            Timber.w(exception)
            ""
        }
    }

    fun format(amount: Double,
               languageCode: String? = selectedLanguageCode,
               countryCode: String? = DEFAULT_COUNTRY_CODE,
               currencyCode: String? = DEFAULT_CURRENCY_CODE,
               fractionDigits: Int = DEFAULT_FRACTION_DIGITS): String {
        if (currencyCode == null) {
            decimalFormatter.minimumFractionDigits = fractionDigits
            decimalFormatter.maximumFractionDigits = DEFAULT_FRACTION_DIGITS
            return decimalFormatter.format(amount)
        }

        val usedLanguageCode = languageCode ?: selectedLanguageCode
        val usedCountryCode = countryCode ?: DEFAULT_COUNTRY_CODE

        if (usedLanguageCode != selectedLanguageCode ||
            usedCountryCode != selectedLocale.country ||
            currencyCode != currency.currencyCode) {
            recreateCurrencyAndLanguage(usedLanguageCode, usedCountryCode, currencyCode)
        }
        currencyFormatter.minimumFractionDigits = fractionDigits
        currencyFormatter.maximumFractionDigits = DEFAULT_FRACTION_DIGITS
        return currencyFormatter.format(amount)
    }

    private fun setDefaultCurrencyAndLanguage() {
        recreateCurrencyAndLanguage(DEFAULT_LANGUAGE_CODE,
            DEFAULT_COUNTRY_CODE,
            DEFAULT_CURRENCY_CODE)
        currencyFormatter.minimumFractionDigits = DEFAULT_FRACTION_DIGITS
        currencyFormatter.maximumFractionDigits = DEFAULT_FRACTION_DIGITS
    }

    private fun recreateCurrencyAndLanguage(languageCode: String,
                                            countryCode: String,
                                            currencyCode: String) {
        Timber.i("Recreating...Language Code: $languageCode, Country Code: $countryCode, Currency Code: $currencyCode")

        try {
            selectedLocale = Locale(languageCode, countryCode)
            selectedLanguageCode = languageCode
        } catch (exception: Exception) {
            Timber.i(exception)
            selectedLocale = Locale(selectedLanguageCode)
        }

        currency = try {
            Currency.getInstance(currencyCode)
        } catch (exception: Exception) {
            Timber.i(exception)
            Currency.getInstance(DEFAULT_CURRENCY_CODE)
        }

        currencyFormatter = NumberFormat.getCurrencyInstance(selectedLocale)
        currencyFormatter.currency = currency
    }
}