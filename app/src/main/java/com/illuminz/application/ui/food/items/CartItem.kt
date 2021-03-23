package com.illuminz.application.ui.food.items

import com.core.extensions.gone
import com.core.extensions.orZero
import com.core.extensions.visible
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_cart.view.*

class CartItem(
    val serviceCategoryItem: ServiceCategoryItemDto,
    var callback: Callback,
    private val fragmentTag: String? = null
) : Item(), AddMenuItemView.Callback {

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(serviceCategoryItem.quantity, true)
            if (fragmentTag!=LaundryFragment.TAG) {
                viewHolder.itemView.tvPrice.text = CurrencyFormatter.format(
                    amount = serviceCategoryItem.price.orZero() * serviceCategoryItem.quantity.toDouble(),
                    currencyCode = "INR"
                )
            } else {
                viewHolder.itemView.tvPrice.text = CurrencyFormatter.format(
                    amount = getPrice() * serviceCategoryItem.quantity.toDouble(),
                    currencyCode = "INR"
                )
            }
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = serviceCategoryItem.itemName
            var price = 0.0
            quantityView.setCallback(this@CartItem)
            quantityView.setQuantity(serviceCategoryItem.quantity, false)

            when (fragmentTag) {
                FoodListFragment.TAG -> {
                    ivFoodType.visible()
                    ivFoodType.setImageResource(R.drawable.ic_vegsymbol)
                    price = serviceCategoryItem.price.orZero().toDouble()
                }
                LaundryFragment.TAG -> {
                    ivFoodType.gone()
                    price = getPrice()
                }

                else -> {
                    ivFoodType.gone()
                    price = serviceCategoryItem.price.orZero().toDouble()
                }
            }
            tvPrice.text = CurrencyFormatter.format(
                amount = price * serviceCategoryItem.quantity.toDouble(),
                currencyCode = "INR"
            )
        }
    }

    override fun getLayout(): Int = R.layout.item_food_cart

    interface Callback {
        fun onIncreaseCartItemClicked(
            serviceCategoryItem: ServiceCategoryItemDto,
            laundryItem: Boolean = false
        )

        fun onDecreaseCartItemClicked(
            serviceCategoryItem: ServiceCategoryItemDto,
            laundryItem: Boolean = false
        )
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        notifyChanged(QuantityChangedPayload)
        serviceCategoryItem.quantity += 1
        if (fragmentTag == LaundryFragment.TAG)
            callback.onIncreaseCartItemClicked(serviceCategoryItem, true)
        else
            callback.onIncreaseCartItemClicked(serviceCategoryItem)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        notifyChanged(QuantityChangedPayload)
        serviceCategoryItem.quantity -= 1
        if (fragmentTag == LaundryFragment.TAG)
            callback.onDecreaseCartItemClicked(serviceCategoryItem, true)
        else
            callback.onDecreaseCartItemClicked(serviceCategoryItem)
    }

    private fun getPrice(): Double {
        return if (serviceCategoryItem.ironingPrice != null) {
            serviceCategoryItem.ironingPrice.orZero()
        } else {
            serviceCategoryItem.washIroningPrice.orZero()
        }
    }
}