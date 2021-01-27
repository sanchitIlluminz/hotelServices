package com.core.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import com.core.extensions.setColorFilterMultiply

class LoadingDialog(
    context: Context,
    @ColorInt progressColor: Int = Color.WHITE
) {
    private val progressBar by lazy {
        ProgressBar(context).apply {
            indeterminateDrawable?.setColorFilterMultiply(progressColor)
        }
    }

    private val dialog by lazy {
        Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setContentView(progressBar)
        }
    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}