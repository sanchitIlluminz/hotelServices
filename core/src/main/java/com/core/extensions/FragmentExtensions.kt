package com.core.extensions

import androidx.fragment.app.Fragment

fun Fragment.isNetworkActive(): Boolean {
    return requireActivity().isNetworkActive()
}

fun Fragment.isNetworkActiveWithMessage(): Boolean {
    return requireActivity().isNetworkActiveWithMessage()
}

