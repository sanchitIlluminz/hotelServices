package com.illuminz.application.ui.food.items

import com.illuminz.application.R
import com.core.utils.MealType
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_food_timing.view.*

class FoodTimingItem(
    private val mealType: MealType,
    private val timing: String? = null,
    var included:Boolean = false

): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvFoodTiming.text = timing
            when(mealType){
                MealType.BREAKFAST ->{
                    tvFoodTimingLabel.text = context.getString(R.string.breakfast)
                    tvFoodTimingLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_breakfast,0,0,0)
                }
                MealType.LUNCH->{
                    tvFoodTimingLabel.text = context.getString(R.string.lunch)
                    tvFoodTimingLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lunch,0,0,0)
                }
                MealType.DINNER ->{
                    tvFoodTimingLabel.text = context.getString(R.string.dinner)
                    tvFoodTimingLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_dinner,0,0,0)
                }
            }

        }
    }

    override fun getLayout(): Int = R.layout.item_food_timing
}