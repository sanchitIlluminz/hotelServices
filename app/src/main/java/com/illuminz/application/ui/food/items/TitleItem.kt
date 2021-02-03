package com.illuminz.application.ui.food.items

import com.core.extensions.invisible
import com.core.extensions.visible
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_title.view.*

class TitleItem (
    private val title: String,
    private val items: String? = null
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = title
            if (items!=null) {
                tvItemNumber.visible()
                tvItemNumber.text = items
            }else{
                tvItemNumber.invisible()
            }

        }
    }

    override fun getLayout(): Int = R.layout.item_title

//    override fun getSpanSize(spanCount: Int, position: Int): Int {
//        return 6
//    }
}