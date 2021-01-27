package com.core.ui.custom

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.core.R
import kotlinx.android.synthetic.main.layout_app_toast.view.*

class AppToast(
    private val context: Context,
    private var lifecycle: Lifecycle? = null
) : LifecycleObserver {
    companion object {
        const val LENGTH_SHORT = Toast.LENGTH_SHORT
        const val LENGTH_LONG = Toast.LENGTH_LONG

        private var displayedToast: Toast? = null
    }

    init {
        lifecycle?.addObserver(this)
    }

    fun show(
        text: String,
        @DrawableRes
        textIconResId: Int? = null,
        duration: Int = LENGTH_SHORT,
        buttonText: String? = null,
        @DrawableRes
        buttonIconResId: Int? = null
    ): Toast {
        displayedToast?.cancel()

        val toast = Toast(context.applicationContext)
        val toastView = View.inflate(context, R.layout.layout_app_toast, null)
        toastView.tvMessage.text = text
        if (textIconResId != null) {
            toastView.tvMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(
                textIconResId,
                0,
                0,
                0
            )
        }

        toastView.btnAction.isVisible = buttonText != null || buttonIconResId != null
        toastView.btnAction.text = buttonText
        if (buttonIconResId != null) {
            toastView.btnAction.setCompoundDrawablesRelativeWithIntrinsicBounds(
                buttonIconResId,
                0,
                0,
                0
            )
        }

        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.view = toastView
        toast.setMargin(0f, 0f)
        toast.duration = duration
        toast.show()

        displayedToast = toast

        return toast
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        displayedToast?.cancel()
        displayedToast = null
        lifecycle?.removeObserver(this)
        lifecycle = null
    }
}