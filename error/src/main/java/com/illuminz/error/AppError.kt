package com.illuminz.error

sealed class AppError {
    data class ApiError(val statusCode: Int, val message: String) : AppError()
    data class ApiFailure(val throwable: Throwable) : AppError()
    data class GetFCMTokenFailure(val throwable: Throwable?) : AppError()
    object ErrorProcessingImage : AppError()
}