package com.core.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.core.di.ViewModelFactory
import com.core.ui.custom.LoadingDialog
import com.illuminz.error.AppError
import com.illuminz.error.ErrorHandler
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class DaggerBaseFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var errorHandler: ErrorHandler

    private var loadingDialog: LoadingDialog? = null

    @LayoutRes
    abstract fun getLayoutResId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(requireActivity())
        }
        loadingDialog?.show()
    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    fun handleError(appError: AppError?, view: View? = null) {
        errorHandler.handleError(appError, requireActivity(), view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog?.dismiss()
    }

}