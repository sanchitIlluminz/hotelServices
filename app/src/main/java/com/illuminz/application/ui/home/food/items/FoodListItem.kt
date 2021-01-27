package com.illuminz.application.ui.home.food.items

import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_list.view.*
import kotlinx.android.synthetic.main.item_food_list.view.quantityView
import kotlinx.android.synthetic.main.item_food_list.view.tvPrice

class FoodListItem(
    private val title: String,
    private val price: Double,
    private var quantity: Int = 0,
    val callback: Callback,
    private val foodItem:Boolean? = true,
    private var veg:Boolean? = true
) : Item(), AddMenuItemView.Callback {

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
            tvPrice.text = CurrencyFormatter.format(amount = price, currencyCode = "INR")

            quantityView.setCallback(this@FoodListItem)
            quantityView.setQuantity(quantity, false)

            if (foodItem==false){
                ivImage.setImageResource(R.drawable.ic_kurta)
            }else if (foodItem==true && veg==true){
                ivImage.setImageResource(R.drawable.ic_vegsymbol)
            }else{
                ivImage.setImageResource(R.drawable.ic_eggsymbol)
            }
        }

    }

//    override fun getSpanSize(spanCount: Int, position: Int): Int {
//        return 6
//    }

    override fun getLayout(): Int = R.layout.item_food_list

    interface Callback {
        fun onIncreaseMenuItemClicked(count: Int)
        fun onDecreaseMenuItemClicked(count: Int)
    }

    override fun onIncreaseMenuItemQuantityClicked() {
        quantity = quantity?.plus(1)
        notifyChanged(QuantityChangedPayload)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        quantity = quantity?.minus(1)
        notifyChanged(QuantityChangedPayload)
    }
}