package com.illuminz.application.ui.home.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_service_title.view.*

class ServiceTitleItem (
    private val title: String
        ): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = title
        }
    }

    override fun getLayout(): Int = R.layout.item_service_title

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 6
    }
}