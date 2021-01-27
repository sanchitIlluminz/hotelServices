package com.illuminz.application.ui.home.bar.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_drink_type.view.*

class DrinkTypeItem(
    private val drinkType: String,
    private val quantity: Int
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
       viewHolder.itemView.apply {
           tvDrinkType.text = drinkType
           tvTotalDrinks.text = "( $quantity )"
       }
    }

    override fun getLayout(): Int = R.layout.item_drink_type
}