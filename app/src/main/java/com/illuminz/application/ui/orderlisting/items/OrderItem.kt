package com.illuminz.application.ui.orderlisting.items

import com.illuminz.application.R
import com.illuminz.data.models.response.FoodCartResponse
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_massage_title.view.*

class OrderItem(
    var title:String,
    var orderResponse:FoodCartResponse? = null
):Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = title
        }
    }

    override fun getLayout(): Int = R.layout.item_massage_title
}