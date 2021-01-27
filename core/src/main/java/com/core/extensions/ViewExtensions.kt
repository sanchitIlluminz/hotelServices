package com.core.extensions

import android.content.Context
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.shortSnackbar(
    text: CharSequence, @DrawableRes
    textIconResId: Int? = null
) {

    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
    val snackLayout = snackbar.view
    val textView =
        snackLayout.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textIconResId?.let { textView.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0) }
    textView.compoundDrawablePadding = this.context.dpToPx(8)
    textView.maxLines = 3
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snackbar.show()
}

fun View.shortSnackbar(@StringRes resId: Int) {
    shortSnackbar(context.getString(resId))
}

fun View.longSnackbar(
    text: CharSequence, @DrawableRes
    textIconResId: Int? = null
) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_LONG)
    val snackLayout = snackbar.view
    val textView =
        snackLayout.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textIconResId?.let { textView.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0) }
    textView.compoundDrawablePadding = this.context.dpToPx(8)
    textView.maxLines = 3
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snackbar.show()
}

fun View.longSnackbar(@StringRes resId: Int) {
    longSnackbar(context.getString(resId))
}

fun View.showKeyboard(forced: Boolean = false) {
    requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (forced) {
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    } else {
        inputMethodManager?.showSoftInput(this, 0)
    }
}

fun View.showKeyboardImplicit() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    requestFocus()
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
}

fun ViewGroup.inflate(res: Int): View {
    return LayoutInflater.from(context).inflate(res, this, false)
}

fun View.isRtl() = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL

inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun View.setMarginBottom(bottomTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, 0, 0, bottomTop)
    this.layoutParams = menuLayoutParams
}

fun View.setMargins(margin: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(margin, margin, margin, margin)
    this.layoutParams = menuLayoutParams
}

fun View.performHapticFeedback() {
    performHapticFeedback(
        HapticFeedbackConstants.VIRTUAL_KEY,
        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
    )
}