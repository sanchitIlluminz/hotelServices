package com.illuminz.application.ui.food.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_timings_list.view.*

class FoodTimingListItem: Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

        }
    }

    override fun getLayout(): Int = R.layout.item_food_timings_list
}