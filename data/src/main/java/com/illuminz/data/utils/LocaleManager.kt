package com.illuminz.data.utils

import android.app.Application
import android.content.Context
import com.yariksoffice.lingver.Lingver
import timber.log.Timber

object LocaleManager {
    private const val DEFAULT_LANGUAGE_CODE = "en"
    val LANGUAGES = mapOf(
        DEFAULT_LANGUAGE_CODE to "English",
        "ar" to "عربى"
    )

    fun init(application: Application) {
        Lingver.init(application, DEFAULT_LANGUAGE_CODE)
    }

    fun getLanguageCode(): String {
        return Lingver.getInstance().getLanguage()
    }

    fun setLanguageCode(context: Context, languageCode: String) {
        if (LANGUAGES.containsKey(languageCode)) {
            Lingver.getInstance().setLocale(context, languageCode)
        } else {
            Timber.w("Invalid language code provided")
        }
    }

    fun resetLanguageCode(context: Context) {
        setLanguageCode(context, DEFAULT_LANGUAGE_CODE)
    }
}