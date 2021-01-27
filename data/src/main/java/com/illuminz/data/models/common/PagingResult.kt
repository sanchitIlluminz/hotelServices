package com.illuminz.data.models.common

data class PagingResult<out T>(
    val isFirstPage: Boolean,
    val result: T? = null
)