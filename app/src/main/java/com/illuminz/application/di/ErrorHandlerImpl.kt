package com.illuminz.application.di

import android.content.Context
import android.content.Intent
import android.view.View
import com.core.extensions.clearNotifications
import com.core.extensions.longSnackbar
import com.core.extensions.longToast
import com.illuminz.application.BuildConfig
import com.illuminz.application.R
import com.illuminz.application.ui.welcome.WelcomeActivity
import com.illuminz.data.remote.SocketManager
import com.illuminz.data.repository.UserRepository
import com.illuminz.error.AppError
import com.illuminz.error.ErrorHandler
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class ErrorHandlerImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val socketManager: SocketManager
) : ErrorHandler {
    override fun handleError(error: AppError?, context: Context, view: View?) {
        Timber.d(error.toString())
        when (error) {
            is AppError.ApiError -> {
                when (error.statusCode) {
                    HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                        context.longToast(error.message)
                        context.clearNotifications()
                        userRepository.clear()
                        socketManager.disconnect()

                        // Navigate to welcome activity
                        val intent = Intent(context, WelcomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }

                    else -> {
                        showMessage(error.message, context, view)
                    }
                }
            }

            is AppError.ApiFailure -> {
                // Ignore displaying toast for api cancel
                if (error.throwable !is CancellationException) {
                    showMessageWithDebugCheck(error.throwable.localizedMessage, context, view)
                }
            }

            is AppError.GetFCMTokenFailure -> {
                showMessage(error.throwable?.localizedMessage, context, view)
            }

            null -> {
                Timber.d("Received error is null")
            }
        }
    }

    private fun showMessageWithDebugCheck(message: String?, context: Context, view: View?) {
        if (BuildConfig.DEBUG) {
            showMessage(message, context, view)
        } else {
            showMessage(context.getString(R.string.message_something_went_wrong), context, view)
        }
    }

    private fun showMessage(message: String?, context: Context, view: View?) {
        if (message.isNullOrBlank()) {
            Timber.d("Received message was null or blank")
            return
        }

        if (view == null) {
            context.longToast(message)
        } else {
            view.longSnackbar(message, R.drawable.ic_error)
        }
    }
}