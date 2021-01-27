package com.illuminz.application.ui.payment

import android.os.Bundle
import android.view.View
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R

class PaymentMethodFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "PaymentMethodFragment"
        fun newInstance(): PaymentMethodFragment{
            return PaymentMethodFragment()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_payment_method


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}