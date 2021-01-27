package com.illuminz.data.models.common

data class ApiResponse<T>(
    val message: String?,
    val error: String?,
    val data: T?
)