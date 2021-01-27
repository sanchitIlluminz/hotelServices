package com.illuminz.data.models.common

/**
 * Status of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
 */
sealed class Status {
    object LOADING : Status()
    object SUCCESS : Status()
    object ERROR : Status()
}