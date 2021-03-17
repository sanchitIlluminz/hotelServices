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
    private val fragmentTag:String? = null
): Item(), AddMenuItemView.Callback {

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(serviceCategoryItem.quantity, true)
            viewHolder.itemView.tvPrice.text = CurrencyFormatter.format(
                amount = serviceCategoryItem.price.orZero()* serviceCategoryItem.quantity.toDouble(),
                currencyCode = "INR"
            )
        } else {
            super.bind(viewHolder, position, payloads)
        }
//        callback.onDecreaseCartItemClicked(serviceCategoryItem.quantity)

    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = serviceCategoryItem.name
            tvPrice.text = CurrencyFormatter.format(
                amount = serviceCategoryItem.price.orZero()* serviceCategoryItem.quantity.toDouble(),
                currencyCode = "INR"
            )

            quantityView.setCallback(this@CartItem)
            quantityView.setQuantity(serviceCategoryItem.quantity, false)

            when(fragmentTag){
                FoodListFragment.TAG ->{
                    ivFoodType.visible()
                    ivFoodType.setImageResource(R.drawable.ic_vegsymbol)
//                    tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_vegsymbol,0,0,0)
                }
                LaundryFragment.TAG ->{
                    ivFoodType.visible()
                    ivFoodType.setImageResource(R.drawable.ic_shirt)
//                    tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_shirt,0,0,0)
                }
                else->{
                    ivFoodType.gone()
//                    tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
                }
            }


        }
    }

    override fun getLayout(): Int = R.layout.item_food_cart


    interface Callback {
        fun onIncreaseCartItemClicked(serviceCategoryItem: ServiceCategoryItemDto)
        fun onDecreaseCartItemClicked(serviceCategoryItem: ServiceCategoryItemDto)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        notifyChanged(QuantityChangedPayload)
        serviceCategoryItem.quantity += 1
        callback.onIncreaseCartItemClicked(serviceCategoryItem)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        notifyChanged(QuantityChangedPayload)
        serviceCategoryItem.quantity -= 1
        callback.onDecreaseCartItemClicked(serviceCategoryItem)
    }
}