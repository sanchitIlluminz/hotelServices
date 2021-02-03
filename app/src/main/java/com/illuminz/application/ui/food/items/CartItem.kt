package com.illuminz.application.ui.food.items

import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food_cart.view.*
import kotlinx.android.synthetic.main.item_food_cart.view.quantityView
import kotlinx.android.synthetic.main.item_food_cart.view.tvPrice
import kotlinx.android.synthetic.main.item_food_cart.view.tvTitle
import kotlinx.android.synthetic.main.item_food_list.view.*

class CartItem(
    private val title : String,
    private val price : Double,
    private var quantity : Int = 0,
    val callback: Callback,
    private val fragmentTag:String? = null
): Item(), AddMenuItemView.Callback {

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(quantity, true)
        } else {
            super.bind(viewHolder, position, payloads)
        }
        callback.onDecreaseMenuItemClicked(quantity)

    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = title
            tvPrice.text = CurrencyFormatter.format(amount = price,
                currencyCode = "INR")
//            quantityTicker.text = quantity.toString()
//
//
//            tvMinus.setOnClickListener(this@FoodCartItem)
//            tvPlus.setOnClickListener(this@FoodCartItem)

            quantityView.setCallback(this@CartItem)
            quantityView.setQuantity(quantity, false)

            when(fragmentTag){
                FoodListFragment.TAG ->{
                    tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_vegsymbol,0,0,0)
                }
                LaundryFragment.TAG ->{
                    tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_shirt,0,0,0)
                }
                else->{
                    tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
                }
            }


        }
    }

    override fun getLayout(): Int = R.layout.item_food_cart
//    override fun onClick(v: View?) {
//
//        when(v?.id){
//            R.id.tvPlus ->{
//                quantity = quantity?.plus(1)
//            }
//            R.id.tvMinus ->{
//                if (quantity!=0 && quantity!! >0
//                ){
//                quantity = quantity?.minus(1)
//                }
//            }
//        }
//    }

    interface Callback {
        fun onIncreaseMenuItemClicked(count: Int)
        fun onDecreaseMenuItemClicked(count: Int)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        quantity = quantity.plus(1)
        notifyChanged(QuantityChangedPayload)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        quantity = quantity.minus(1)
        notifyChanged(QuantityChangedPayload)
    }
}