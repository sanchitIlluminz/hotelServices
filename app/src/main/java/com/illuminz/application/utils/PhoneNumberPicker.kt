package com.illuminz.application.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import timber.log.Timber

object PhoneNumberPicker {
    private var PHONE_NUMBER_UTIL: PhoneNumberUtil? = null

    private const val REQ_CODE_PHONE_NUMBER = 100

    fun showPhoneNumberPicker(fragment: Fragment) {
        try {
            if (PHONE_NUMBER_UTIL == null) {
                PHONE_NUMBER_UTIL =
                    PhoneNumberUtil.createInstance(fragment.requireActivity().applicationContext)
            }
            val hintRequest = HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build()
            val apiClient = GoogleApiClient.Builder(fragment.requireContext())
                .addApi(Auth.CREDENTIALS_API)
                .build()
            val intent = Auth.CredentialsApi.getHintPickerIntent(
                apiClient, hintRequest
            )
            fragment.startIntentSenderForResult(
                intent.intentSender,
                REQ_CODE_PHONE_NUMBER, null, 0, 0, 0, null
            )
        } catch (exception: Exception) {
            Timber.w(exception)
        }
    }

    fun parsePhoneNumber(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        countryCodeName: String
    ): Pair<Int, String>? {
        if (requestCode == REQ_CODE_PHONE_NUMBER && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val credential: Credential? = data.getParcelableExtra(Credential.EXTRA_KEY)
                val fullPhoneNumber = credential?.id
                if (fullPhoneNumber != null) {
                    val phoneNumber = PHONE_NUMBER_UTIL?.parse(fullPhoneNumber, countryCodeName)
                    if (phoneNumber != null && PHONE_NUMBER_UTIL?.isValidNumber(phoneNumber) == true) {
                        return phoneNumber.countryCode to phoneNumber.nationalNumber.toString()
                    }
                }
            } catch (exception: Exception) {
                Timber.w(exception)
            }
        }
        return null
    }
}