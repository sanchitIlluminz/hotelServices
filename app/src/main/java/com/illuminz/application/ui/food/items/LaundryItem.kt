package com.illuminz.application.ui.food.items

import com.core.extensions.orZero
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.ServiceCategoryDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_laundry.view.*

class LaundryItem(
    var serviceCategoryDto: ServiceCategoryDto,
    val laundryType: String,
    val callback: Callback
) : Item(), AddMenuItemView.Callback {

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(serviceCategoryDto.quantity, true)
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {

            if (laundryType == AppConstants.LAUNDARY_ONLY_IRON) {
                tvPrice.text = CurrencyFormatter.format(
                    amount = serviceCategoryDto.ironingPrice.orZero(),
                    currencyCode = "INR"
                )
            } else {
                tvPrice.text = CurrencyFormatter.format(
                    amount = serviceCategoryDto.washIroningPrice.orZero(),
                    currencyCode = "INR"
                )
            }
            tvTitle.text = serviceCategoryDto.itemName

            quantityView.setCallback(this@LaundryItem)
            quantityView.setQuantity(serviceCategoryDto.quantity, false)
        }
    }

    override fun getLayout(): Int = R.layout.item_laundry

    interface Callback {
        fun onIncreaseLaundryItemClicked(laundryItem: LaundryItem)
        fun onDecreaseLaundryItemClicked(laundryItem: LaundryItem)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        serviceCategoryDto.quantity += 1
        notifyChanged(QuantityChangedPayload)
        callback.onIncreaseLaundryItemClicked(this)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        serviceCategoryDto.quantity -= 1
        notifyChanged(QuantityChangedPayload)
        callback.onDecreaseLaundryItemClicked(this)
    }
}