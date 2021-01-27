package com.core.ui.base

import android.content.Context
import android.view.View
import com.core.di.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illuminz.error.AppError
import com.illuminz.error.ErrorHandler
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class DaggerBaseBottomSheetDialogFragment : BottomSheetDialogFragment(),
    HasAndroidInjector {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var errorHandler: ErrorHandler

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    fun handleError(appError: AppError?, view: View? = null) {
        errorHandler.handleError(appError, requireActivity(), view)
    }
}