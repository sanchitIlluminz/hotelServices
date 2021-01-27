package com.core.extensions

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.textfield.TextInputLayout

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun TextInputLayout.updateError(error: CharSequence?) {
    if (error == null) {
        isErrorEnabled = false
        this.error = null
    } else {
        isErrorEnabled = true
        this.error = error
    }
}



@SuppressLint("RestrictedApi")
fun EditText.updateBackgroundColor(@ColorInt colorInt: Int?) {
    if (colorInt == null) {
        DrawableCompat.clearColorFilter(background)
        refreshDrawableState()
    } else {
        val filter = AppCompatDrawableManager.getPorterDuffColorFilter(
            colorInt,
            PorterDuff.Mode.SRC_IN
        )
        background.colorFilter = filter
    }
}