package com.illuminz.application.ui.cart.items

import com.core.extensions.gone
import com.core.extensions.orZero
import com.core.extensions.visible
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.CartItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_cart.view.*

class CartItem(
    val itemDetails: CartItemDto,
    var callback: Callback,
    private val fragmentTag: String? = null
) : Item(), AddMenuItemView.Callback {

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(itemDetails.quantity.orZero(), true)
            if (fragmentTag!=LaundryFragment.TAG) {
                viewHolder.itemView.tvPrice.text = CurrencyFormatter.format(
                    amount = itemDetails.price.orZero() * itemDetails.quantity.orZero().toDouble(),
                    currencyCode = "INR"
                )
            } else {
                viewHolder.itemView.tvPrice.text = CurrencyFormatter.format(
                    amount = getPrice(itemDetails) * itemDetails.quantity.orZero().toDouble(),
                    currencyCode = "INR"
                )
            }
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = itemDetails.itemName
            var price = 0.0
            quantityView.setCallback(this@CartItem)
            quantityView.setQuantity(itemDetails.quantity.orZero(), false)

            when (fragmentTag) {
                FoodListFragment.TAG -> {
                    ivFoodType.visible()
                    ivFoodType.setImageResource(R.drawable.ic_vegsymbol)
                    price = itemDetails.price.orZero()
                }
                LaundryFragment.TAG -> {
                    ivFoodType.gone()
                    if (itemDetails.laundryType == AppConstants.LAUNDRY_ONLY_IRON)
                        tvTitle.text = "${itemDetails.itemName} (only Iron)"
                    else
                        tvTitle.text = "${itemDetails.itemName} (wash & Iron)"
                    price = getPrice(itemDetails)
                }

                else -> {
                    ivFoodType.gone()
                    price = itemDetails.price.orZero()
                }
            }
            tvPrice.text = CurrencyFormatter.format(
                amount = price * itemDetails.quantity.orZero().toDouble(),
                currencyCode = "INR"
            )
        }
    }

    override fun getLayout(): Int = R.layout.item_food_cart

    interface Callback {
        fun onIncreaseCartItemClicked(
            cartItem: CartItemDto,
            laundryItem: Boolean = false
        )

        fun onDecreaseCartItemClicked(
            cartItem: CartItem,
            laundryItem: Boolean = false
        )
    }

    override fun onIncreaseMenuItemQuantityClicked() {
//        notifyChanged(QuantityChangedPayload)
        itemDetails.quantity = itemDetails.quantity?.plus(1)
        if (fragmentTag == LaundryFragment.TAG)
            callback.onIncreaseCartItemClicked(itemDetails, true)
        else
            callback.onIncreaseCartItemClicked(itemDetails)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        itemDetails.quantity = itemDetails.quantity?.minus(1)
        if (fragmentTag == LaundryFragment.TAG)
            callback.onDecreaseCartItemClicked(this@CartItem, true)
        else
            callback.onDecreaseCartItemClicked(this@CartItem)
    }

    private fun getPrice(itemDetails: CartItemDto): Double {
        return if (itemDetails.laundryType == AppConstants.LAUNDRY_ONLY_IRON) {
            itemDetails.ironingPrice.orZero()
        } else {
            itemDetails.washIroningPrice.orZero()
        }
    }
}