package com.illuminz.application.ui.bar.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_bar.view.*

class BarItem(
    private val drinkType: String,
    private val qt1: String
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvDrink.text = drinkType
            tvQuantity1.text = qt1
        }
    }

    override fun getLayout(): Int = R.layout.item_bar


}