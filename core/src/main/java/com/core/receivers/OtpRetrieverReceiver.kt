package com.core.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import timber.log.Timber
import java.util.regex.Pattern

class OtpRetrieverReceiver : BroadcastReceiver() {
    companion object {
        private val OTP_PATTERN = Pattern.compile("(\\d{4})")
        private val CALLBACKS by lazy { mutableSetOf<Callback>() }
        private var IS_RUNNING = false

        fun addCallback(callback: Callback) {
            CALLBACKS.add(callback)
        }

        fun removeCallback(callback: Callback) {
            CALLBACKS.remove(callback)
        }

        fun startSmsRetriever(context: Context) {
            if (IS_RUNNING) {
                Timber.w("SMS retriever is already running")
                return
            }
            val task = SmsRetriever.getClient(context).startSmsRetriever()
            task.addOnSuccessListener {
                Timber.i("Successfully started retriever")
                IS_RUNNING = true
            }
            task.addOnFailureListener {
                Timber.w(it)
                IS_RUNNING = false
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status
            IS_RUNNING = false
            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    //This is the full message
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String
                    val otp = getOtp(message)
                    if (!otp.isNullOrBlank()) {
                        CALLBACKS.forEach { it.onOtpReceived(otp) }
                    }
                }

                CommonStatusCodes.TIMEOUT ->
                    Timber.w("Waiting for SMS timed out (5 minutes)")

                CommonStatusCodes.API_NOT_CONNECTED ->
                    Timber.w("API NOT CONNECTED")

                CommonStatusCodes.NETWORK_ERROR ->
                    Timber.w("NETWORK ERROR")

                CommonStatusCodes.ERROR ->
                    Timber.w("SOMETHING WENT WRONG")
            }
        }
    }

    private fun getOtp(message: String?): String? {
        message ?: return null
        val matcher = OTP_PATTERN.matcher(message)
        return if (matcher.find()) {
            matcher.group(0)?.trim()
        } else {
            null
        }
    }

    interface Callback {
        fun onOtpReceived(otp: String)
    }
}