package com.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.google.android.material.textfield.TextInputEditText

class ClearFocusTextInputEditText @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null
) : TextInputEditText(context,attr) {
//    constructor(context: Context) : super(context)
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
//    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
//        context,
//        attrs,
//        defStyle
//    )

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
        }
        return super.onKeyPreIme(keyCode, event)
    }
}