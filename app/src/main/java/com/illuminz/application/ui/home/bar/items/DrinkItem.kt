package com.illuminz.application.ui.home.bar.items

import com.illuminz.application.R
import com.illuminz.application.ui.custom.AddMenuItemView
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_bar.view.tvDrink
import kotlinx.android.synthetic.main.item_drink.view.*
import kotlinx.android.synthetic.main.item_drink.view.quantityView
import kotlinx.android.synthetic.main.item_food.view.*

class DrinkItem(
    val drinkName: String,
    val price1: Double,
    val price2: Double,
    val drinkVol1: String,
    val drinkVol2: String,
    val drinkType: String,
    var quantity: Int = 0,
    val callback: Callback
) : Item(), AddMenuItemView.Callback {

    private lateinit var drinkOrder: ArrayList<DrinkOrder>


    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == QuantityChangedPayload) {
            viewHolder.itemView.quantityView.setQuantity(quantity, true)
        } else {
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvDrink.text = drinkName
            tvPrice1.text = CurrencyFormatter.format(amount = price1, currencyCode = "INR")

            quantityView.setCallback(this@DrinkItem)
            quantityView.setQuantity(quantity,true)

            drinkOrder= arrayListOf(
                            DrinkOrder(
                                drinkName = drinkName,
                                drinkVol = drinkVol2,
                                price = price2,
                                quantity = quantity),
                            DrinkOrder(
                                drinkName = drinkName,
                                drinkVol = drinkVol1,
                                price = price1,
                                quantity = quantity)
                            )
        }
    }

    override fun getLayout(): Int = R.layout.item_drink

    interface Callback{
        fun addDrink(drinkList:ArrayList<DrinkOrder>, drinkName: String)
    }

    override fun onIncreaseMenuItemQuantityClicked() {

//        if (quantity==0){
//            callback.addDrink(drinkOrder)
//        }else{
//            quantity = quantity?.plus(1)
//            notifyChanged(QuantityChangedPayload)
//        }
        quantity = quantity.plus(1)
        notifyChanged(QuantityChangedPayload)


        callback.addDrink(drinkOrder,drinkName)
    }

    override fun onDecreaseMenuItemQuantityClicked() {
        quantity = quantity.minus(1)
        notifyChanged(QuantityChangedPayload)
    }

    fun setDrinkQuantity(addition:Int){
        quantity = quantity.plus(addition)
    }
}