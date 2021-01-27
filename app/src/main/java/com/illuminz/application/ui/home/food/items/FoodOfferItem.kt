package com.illuminz.application.ui.home.food.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_offer.view.*

class FoodOfferItem(
    private val title:String,
    private val description: String
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
       viewHolder.itemView.apply {
           tvCouponName.text = title
           tvDescription.text = description
       }
    }

//    override fun getSpanSize(spanCount: Int, position: Int): Int {
//        return 2
//    }

    override fun getLayout(): Int = R.layout.item_food_offer
}