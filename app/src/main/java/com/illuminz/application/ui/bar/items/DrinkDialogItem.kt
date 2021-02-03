package com.illuminz.application.ui.bar.items

import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_drink_dialog.view.*

class DrinkDialogItem(
    val drinkOrder: DrinkOrder
) : Item(), AddMenuItemView.Callback {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            tvDrinkPrice.text = CurrencyFormatter.format(amount = drinkOrder.price, currencyCode = "INR")
            tvDrinkQuantity.text = drinkOrder.drinkVol

            quantityView.setCallback(this@DrinkDialogItem)
            quantityView.setQuantity(drinkOrder.quantity, false)

        }

    }

    override fun getLayout(): Int = R.layout.item_drink_dialog

    interface Callback {
        fun onIncreaseMenuItemClicked(count: Int)
        fun onDecreaseMenuItemClicked(count: Int)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        drinkOrder.quantity = drinkOrder.quantity.plus(1)
        notifyChanged(QuantityChangedPayload)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        drinkOrder.quantity = drinkOrder.quantity.minus(1)
        notifyChanged(QuantityChangedPayload)
    }
}