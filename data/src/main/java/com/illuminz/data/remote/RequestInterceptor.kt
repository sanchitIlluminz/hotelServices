package com.illuminz.data.remote

import com.illuminz.data.BuildConfig
import com.illuminz.data.repository.AuthorizationRepository
import com.illuminz.data.utils.LocaleManager
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class RequestInterceptor(
    private val authorizationRepository: AuthorizationRepository
) : Interceptor {
    companion object {
        private const val HEADER_AUTHORIZATION = "authorization"
        private const val HEADER_LANGUAGE_CODE = "Accept-Language"
        private const val HEADER_DEVICE_TYPE = "deviceType"

        // private const val HEADER_OFFSET = "utcoffset"
        private const val HEADER_TIMEZONE = "Timezone"
        private const val HEADER_VERSION_CODE = "androidVersionCode"
        private const val DEVICE_TYPE_ANDROID = "ANDROID"
        private const val VERSION_CODE = BuildConfig.VERSION_CODE.toString()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val authorization = authorizationRepository.getAuthorization()
        requestBuilder.header(HEADER_LANGUAGE_CODE, LocaleManager.getLanguageCode())
        requestBuilder.header(HEADER_TIMEZONE, getTimezone())
        requestBuilder.header(HEADER_DEVICE_TYPE, DEVICE_TYPE_ANDROID)
        requestBuilder.header(HEADER_VERSION_CODE, VERSION_CODE)

        if (authorization != null) {
            Timber.i("Adding authorization to header")
            requestBuilder.header(HEADER_AUTHORIZATION, authorization)
        } else {
            Timber.i("Authorization does not exist")
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun getOffsetInMinutes(): String {
        val offsetInMillis = TimeZone.getDefault().rawOffset.toLong()
        val offsetInMinutes = TimeUnit.MILLISECONDS.toMinutes(offsetInMillis)
        return offsetInMinutes.toString()
    }

    private fun getTimezone(): String {
        return TimeZone.getDefault().id
    }
}