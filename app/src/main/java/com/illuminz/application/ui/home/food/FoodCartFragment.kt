package com.illuminz.application.ui.home.food

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
import com.illuminz.application.ui.home.food.items.FoodCartItem
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.fragment_food_cart.*
import kotlinx.android.synthetic.main.fragment_food_cart.cartBarView
import kotlinx.android.synthetic.main.fragment_food_cart.toolbar
import kotlinx.android.synthetic.main.fragment_food_list.*

class FoodCartFragment : DaggerBaseFragment(),FoodCartItem.Callback, CartBarView.Callback {

    companion object {
        const val TAG = "FoodCartFragment"
        fun newInstance() : FoodCartFragment{
            return FoodCartFragment()
        }

    }

    private lateinit var cartItemAdapter : GroupAdapter<GroupieViewHolder>
    private var totalPrice:Double = 0.00

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setBasicData()
        setListeners()
    }

    private fun setBasicData() {
        tvItemTotal.text = CurrencyFormatter.format(amount = 800.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)
        tvServiceCharges.text = CurrencyFormatter.format(amount = 20.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)
        tvTaxes.text = CurrencyFormatter.format(amount = 10.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)
        tvTotalAmount.text = CurrencyFormatter.format(amount = 830.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)

    }

    private fun initialise() {

        cartBarView.setButtonText(getString(R.string.proceed_to_pay))
        cartBarView.setItemPrice(totalPrice = 820.00,items = 4)

        toolbar.subtitle = resources.getQuantityString(R.plurals.cart_items, 6, 6)
        cartItemAdapter = GroupAdapter()
        rvFoodCart.adapter = cartItemAdapter

        val item = listOf(FoodCartItem(title = "Chocolate and Strawberry Pancake",price = 200.00,quantity = 5,callback = this),
            FoodCartItem(title = "Stuffed Pranthas with Butter - 4 Pc", price = 300.00,quantity = 2,callback = this),
            FoodCartItem(title = "Vegetable Salad with Mint Mayo",price = 100.00,callback = this),
            FoodCartItem(title = "Chocolate and Strawberry Pancake",price = 200.00,quantity = 5,callback = this),
            FoodCartItem(title = "Stuffed Pranthas with Butter - 4 Pc", price = 300.00,quantity = 2,callback = this),
            FoodCartItem(title = "Vegetable Salad with Mint Mayo",price = 100.00,callback = this)
        )

        cartItemAdapter.addAll(item)

    }

    private fun setListeners() {

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        tvCoupon.setOnClickListener {

        }

        cartBarView.setCallback(this)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_food_cart


    override fun onIncreaseMenuItemClicked(count: Int) {
        val a =9
    }

    override fun onDecreaseMenuItemClicked(count: Int) {
        val a =9

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

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onCartBarClick() {
        showConfirmationDialog(getString(R.string.order_placed), getString(R.string.order_will_be_delivered_in_time))
    }
}