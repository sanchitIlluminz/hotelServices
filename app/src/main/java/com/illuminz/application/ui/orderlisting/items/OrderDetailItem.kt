package com.illuminz.application.ui.orderlisting.items

import com.core.extensions.orZero
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.data.models.response.CartItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_order_detail.view.*
import kotlinx.android.synthetic.main.item_order_detail.view.*

class OrderDetailItem(
    var cartItem: CartItemDto
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            tvTitle.text = "${cartItem.quantity}X ${cartItem.itemName}"
            val price =
                if (cartItem.laundryType == null) {
                    cartItem.price.orZero()
                } else {
                    if (cartItem.laundryType == AppConstants.LAUNDRY_ONLY_IRON)
                        cartItem.ironingPrice.orZero()
                    else
                        cartItem.washIroningPrice.orZero()
                }

            tvPrice.text = CurrencyFormatter.format(amount = price)
        }
    }

    override fun getLayout(): Int = R.layout.item_order_detail
}