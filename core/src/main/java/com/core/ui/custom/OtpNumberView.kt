package com.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.core.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.layout_otp_number.view.*

class OtpNumberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    TextInputLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_otp_number, this, true)
    }

    fun setNumber(number: Char) {
        etOtp.setText(number.toString())
    }

    fun getNumber(): String = etOtp.text.toString()
}