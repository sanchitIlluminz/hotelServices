package com.illuminz.application.ui.orderlisting

import android.os.Bundle
import android.view.View
import com.core.extensions.orZero
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.orderlisting.items.OrderDetailItem
import com.illuminz.data.models.response.FoodCartResponse
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_order_detail.*

class OrderDetailFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "OrderDetailFragment"

        private const val KEY_ORDER_RESPONSE = "KEY_ORDER_RESPONSE"

        fun newInstance(foodCartResponse: FoodCartResponse?): OrderDetailFragment {
            val fragment = OrderDetailFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ORDER_RESPONSE, foodCartResponse)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private lateinit var foodCartResponse: FoodCartResponse
    private var totalAmount = 0.0

    override fun getLayoutResId(): Int = R.layout.fragment_order_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
    }

    private fun initialise() {
        adapter = GroupAdapter()
        rvOrders.adapter = adapter

        requireArguments().getParcelable<FoodCartResponse>(KEY_ORDER_RESPONSE)?.let {
            foodCartResponse = it
        }

        foodCartResponse.items?.forEach { cartItem ->
            adapter.add(OrderDetailItem(cartItem))
            totalAmount += cartItem.cartTotal ?: 0.0
        }

        tvItemTotal.text = CurrencyFormatter.format(amount = totalAmount)


    }

}
