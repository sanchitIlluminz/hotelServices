package com.core.extensions

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt


fun Drawable.setColorFilterMultiply(@ColorInt colorInt: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = PorterDuffColorFilter(colorInt, PorterDuff.Mode.MULTIPLY)
        //colorFilter = BlendModeColorFilter(colorInt, BlendMode.MULTIPLY)
    } else {
        @Suppress("Deprecation")
        setColorFilter(colorInt, PorterDuff.Mode.MULTIPLY)
    }
}