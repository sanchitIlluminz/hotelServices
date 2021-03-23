package com.illuminz.application.ui.laundry.items

import com.core.extensions.orZero
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_laundry.view.*

class LaundryItem(
    var serviceCategoryItem: ServiceCategoryItemDto,
    val laundryType: String,
    val callback: Callback
) : Item(), AddMenuItemView.Callback {

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(serviceCategoryItem.quantity, true)
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {

            if (laundryType == AppConstants.LAUNDARY_ONLY_IRON) {
                tvPrice.text = CurrencyFormatter.format(
                    amount = serviceCategoryItem.ironingPrice.orZero(),
                    currencyCode = "INR"
                )
            } else {
                tvPrice.text = CurrencyFormatter.format(
                    amount = serviceCategoryItem.washIroningPrice.orZero(),
                    currencyCode = "INR"
                )
            }
            tvTitle.text = serviceCategoryItem.itemName

            quantityView.setCallback(this@LaundryItem)
            quantityView.setQuantity(serviceCategoryItem.quantity, false)
        }
    }

    override fun getLayout(): Int = R.layout.item_laundry

    interface Callback {
        fun onIncreaseLaundryItemClicked(laundryItem: LaundryItem)
        fun onDecreaseLaundryItemClicked(laundryItem: LaundryItem)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        serviceCategoryItem.quantity += 1
        notifyChanged(QuantityChangedPayload)
        callback.onIncreaseLaundryItemClicked(this)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        serviceCategoryItem.quantity -= 1
        notifyChanged(QuantityChangedPayload)
        callback.onDecreaseLaundryItemClicked(this)
    }
}