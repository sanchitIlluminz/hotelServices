package com.illuminz.data.extensions

import com.illuminz.data.models.common.Resource
import com.illuminz.error.AppError
import org.json.JSONObject
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException

fun Throwable.toApiFailure(): AppError {
    return AppError.ApiFailure(this)
}

fun <T> Response<T>.toApiError(): AppError {
    val statusCode = code()
    val errorMessage = getErrorMessage(errorBody()?.string())
    return AppError.ApiError(statusCode, errorMessage)
}

fun <T> Resource<T>.isConnectionError(): Boolean {
    return error is AppError.ApiFailure && error.throwable.isConnectionException()
}

private fun Throwable.isConnectionException(): Boolean {
    return this is ConnectException || this is SocketException || this is SocketTimeoutException
}

private fun getErrorMessage(errorJson: String?): String {
    if (errorJson.isNullOrBlank()) {
        return ""
    }

    return try {
        val errorJsonObject = JSONObject(errorJson)
        errorJsonObject.getString("message")
    } catch (exception: Exception) {
        ""
    }
}