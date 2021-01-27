package com.core.ui.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.core.ui.custom.LoadingDialog
import com.illuminz.error.AppError
import com.illuminz.error.ErrorHandler
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var errorHandler: ErrorHandler

    private var loadingDialog: LoadingDialog? = null

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        loadingDialog?.show()
    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    fun handleError(appError: AppError?, view: View? = null) {
        errorHandler.handleError(appError, this, view)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
    }
}