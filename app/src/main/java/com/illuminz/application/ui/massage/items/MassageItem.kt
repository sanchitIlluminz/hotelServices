package com.illuminz.application.ui.massage.items

import com.core.extensions.orZero
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.items_massage.view.*
import kotlinx.android.synthetic.main.items_massage.view.quantityView
import kotlinx.android.synthetic.main.items_massage.view.tvTitle

class MassageItem(
    var serviceCategoryItem: ServiceCategoryItemDto,
    val callback: Callback
) : Item(), AddMenuItemView.Callback {

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(serviceCategoryItem.quantity.orZero(), true)
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = serviceCategoryItem.title

            (context.getString(R.string.valid_for) + "1" + context.getString(R.string.person))
                .also { tvPersonCount.text = it }

            ("Duration: " + "${serviceCategoryItem.duration}").also { tvDaysCount.text = it }

            quantityView.setCallback(callback = this@MassageItem)
            quantityView.setQuantity(serviceCategoryItem.quantity, false)

            tvPrice.text = CurrencyFormatter.format(
                amount = serviceCategoryItem.price.orZero().toDouble(),
                currencyCode = "INR",
                countryCode = "IN"
            )
            tvDescription.text = serviceCategoryItem.description.orEmpty()
        }
    }

    interface Callback {
        fun onIncreaseMassageItemClicked(massageItem: MassageItem)
        fun onDecreaseMassageItemClicked(massageItem: MassageItem)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        serviceCategoryItem.quantity += 1
        notifyChanged(QuantityChangedPayload)
        callback.onIncreaseMassageItemClicked(this)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        serviceCategoryItem.quantity -= 1
        notifyChanged(QuantityChangedPayload)
        callback.onDecreaseMassageItemClicked(this)
    }

    override fun getLayout(): Int = R.layout.items_massage
}