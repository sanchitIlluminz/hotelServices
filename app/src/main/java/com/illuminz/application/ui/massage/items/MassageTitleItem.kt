package com.illuminz.application.ui.massage.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_massage_title.view.*

class MassageTitleItem(
    private val title: String
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = title
        }
    }

    override fun getLayout(): Int = R.layout.item_massage_title
}