package com.illuminz.application.ui.home.nearbyplaces.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_nearby_sub_title.view.*

class NearbySubTitleItem(
    private val subTitle: String
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = subTitle
        }

    }

    override fun getLayout(): Int = R.layout.item_nearby_sub_title

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 6
    }
}