package com.core.ui.custom

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.core.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AppAlertDialogBuilder(
    context: Context,
    private val lifecycle: Lifecycle? = null,
    themeResId: Int = R.style.AppTheme_MaterialAlertDialog
) : MaterialAlertDialogBuilder(context, themeResId),
    LifecycleObserver {
    private var dialog: Dialog? = null

    init {
        lifecycle?.addObserver(this)
    }

    override fun create(): AlertDialog {
        val alertDialog = super.create()
        dialog = alertDialog
        return alertDialog
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        dialog?.dismiss()
        lifecycle?.removeObserver(this)
    }
}