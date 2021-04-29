package com.illuminz.application.ui.orderlisting

import android.os.Bundle
import android.view.View
import com.core.extensions.orZero
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.orderlisting.items.OrderDetailItem
import com.illuminz.data.models.response.FoodCartResponse
import com.illuminz.data.models.response.TaxesDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_order_detail.*

class OrderDetailFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "OrderDetailFragment"

        private const val KEY_ORDER_RESPONSE = "KEY_ORDER_RESPONSE"
        private const val KEY_ORDER_TYPE = "KEY_ORDER_TYPE"

        fun newInstance(foodCartResponse: FoodCartResponse?, orderType: Int): OrderDetailFragment {
            val fragment = OrderDetailFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ORDER_RESPONSE, foodCartResponse)
            arguments.putInt(KEY_ORDER_TYPE, orderType)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private lateinit var foodCartResponse: FoodCartResponse
    private var orderType = -1
    private var totalAmount = 0.0
    private var serviceTax = 0.0
    private var tax = 0.0

    override fun getLayoutResId(): Int = R.layout.fragment_order_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initialise() {
        adapter = GroupAdapter()
        rvOrders.adapter = adapter

        requireArguments().getParcelable<FoodCartResponse>(KEY_ORDER_RESPONSE)?.let {
            foodCartResponse = it
        }

        orderType = requireArguments().getInt(KEY_ORDER_TYPE)

        foodCartResponse.items?.forEach { cartItem ->
            adapter.add(OrderDetailItem(cartItem))
            totalAmount += cartItem.cartTotal ?: 0.0
        }

        val taxes =
        if (orderType == AppConstants.ORDER_TYPE_FOOD){
            val foodTax = foodCartResponse.taxes?.foodTaxes
            val liquorTax = foodCartResponse.taxes?.liquorTaxes
            val tax = mutableListOf<TaxesDto>()
            foodTax?.let { tax.addAll(it) }
            liquorTax?.let { tax.addAll(it) }
            tax
        }else{
            foodCartResponse.taxes?.liquorTaxes
        }

        taxes?.forEach { tax ->
            serviceTax += tax.debit.orZero()
        }

        tvServiceCharges.text = CurrencyFormatter.format(amount = serviceTax)
        tvTaxes.text = CurrencyFormatter.format(amount = 0.0)

        tvItemTotal.text = CurrencyFormatter.format(amount = totalAmount)

        val total = totalAmount+serviceTax
        tvTotalAmount.text = CurrencyFormatter.format(amount = total)
    }

}
