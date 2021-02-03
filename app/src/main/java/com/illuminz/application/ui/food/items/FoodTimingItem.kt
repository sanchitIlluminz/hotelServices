package com.illuminz.application.ui.food.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_timings.view.*

class FoodTimingItem: Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvBreakfastTime.text =("8:30 am - 10:30 pm")
            tvLunchTime.text = ("1:30 pm - 2:30 pm")
            tvDinnerTime.text = ("8:30 pm - 11:30 pm")
        }
    }

    override fun getLayout(): Int = R.layout.item_food_timings
}