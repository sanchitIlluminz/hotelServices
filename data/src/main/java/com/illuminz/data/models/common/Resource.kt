package com.illuminz.data.models.common

import com.illuminz.error.AppError

/**
 * A generic class that holds a value with its loading status.
 * */
data class Resource<out T>(val status: Status, val data: T? = null, val error: AppError? = null) {
    companion object {
        fun <T> success(data: T? = null): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(error: AppError?, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                error
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }

    fun isSuccess(): Boolean = status == Status.SUCCESS

    fun isLoading(): Boolean = status == Status.LOADING
}