package com.illuminz.application.ui.home.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_feedback_rating.view.*

class FeedbackRatingItem(
    var rating:Int,
    var selected:Boolean = false
) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvRating.text = rating.toString()
            if (selected)
                tvRating.setBackgroundResource(R.drawable.background_rating_selected)
            else
                tvRating.setBackgroundResource(R.drawable.background_rating_default)
        }
    }

    override fun getLayout(): Int = R.layout.item_feedback_rating
}