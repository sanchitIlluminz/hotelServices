package com.illuminz.error

import android.content.Context
import android.view.View

interface ErrorHandler {
    fun handleError(error: AppError?, context: Context, view: View? = null)
}