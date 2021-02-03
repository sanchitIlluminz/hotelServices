package com.illuminz.application.ui.payment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.fragment_payment_method.*
import kotlinx.android.synthetic.main.fragment_payment_method.cartBarView

class PaymentMethodFragment : DaggerBaseFragment(),CartBarView.Callback {
    companion object {
        const val TAG = "PaymentMethodFragment"
        fun newInstance(): PaymentMethodFragment{
            return PaymentMethodFragment()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_payment_method


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartBarView.setButtonText("Pay Now")
        cartBarView.setItemPrice(totalPrice = 820.00,items = 4)
        cartBarView.setCallback(this)

        setListeners()


    }

    private fun setListeners() {
        toolbar1.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onCartBarClick() {
        showConfirmationDialog(getString(R.string.order_placed), getString(R.string.order_will_be_delivered_in_time))

    }

    private fun showConfirmationDialog(title: String, subtitle: String) {
        val dialog = context?.let { Dialog(it) }

        dialog?.run {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_confirm)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
            }

            tvTitle.text = title
            tvSubtitle.text = subtitle

            btnOkay.setOnClickListener {
                dismiss()
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            show()
        }
    }
}