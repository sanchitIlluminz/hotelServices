package com.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.core.R
import com.core.extensions.afterTextChanged
import com.core.extensions.showKeyboard
import kotlinx.android.synthetic.main.layout_otp_number.view.*
import kotlinx.android.synthetic.main.layout_otp_view.view.*

class OtpView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    private var otpFilledListener: OnOtpFilledListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_otp_view, this, true)

        val numberViews = listOf<OtpNumberView>(otp1, otp2, otp3, otp4)
        numberViews.forEach { numberView ->
            val numberEditText = numberView.etOtp

            if (numberEditText != null) {
                numberEditText.afterTextChanged { text ->
                    if (text.length == 1) {
                        val nextNumberView =
                            numberViews.find { it.id == numberView.nextFocusRightId }
                        if (nextNumberView != null) {
                            nextNumberView.etOtp?.requestFocus()
                        } else {
                            numberEditText.clearFocus()
                        }
                    }
                    notifyOtpFilledListener()
                }

                numberEditText.setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (numberEditText.text.isNullOrBlank()) {
                            val previousNumberView =
                                numberViews.find { it.id == numberView.nextFocusLeftId }
                            previousNumberView?.etOtp?.requestFocus()
                        }
                    }
                    return@setOnKeyListener false
                }
            }
        }
    }

    private fun getNumberViews() = listOf<OtpNumberView>(otp1, otp2, otp3, otp4)

    private fun notifyOtpFilledListener() {
        val otp = getOtp()
        if (otp.length == 4) {
            otpFilledListener?.onOtpFilled(otp)
        }
    }

    fun setOtpFilledListener(listener: OnOtpFilledListener) {
        this.otpFilledListener = listener
    }

    fun getOtp(): String = getNumberViews().joinToString(separator = "") { numberView ->
        numberView.getNumber()
    }

    fun setOtp(otp: String) {
        val numberViews = getNumberViews()
        if (otp.length <= numberViews.size) {
            otp.forEachIndexed { index, number ->
                numberViews[index].setNumber(number)
            }
        }
    }

    fun focusFirstNumber() {
        otp1.showKeyboard(true)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        getNumberViews().forEach { it.isEnabled = enabled }
    }

    interface OnOtpFilledListener {
        fun onOtpFilled(otp: String)
    }
}