package com.illuminz.application.ui.roomcleaning.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_cleaning_popup.view.*

class PopupCleaningItem(
     val time:String
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTime.text = time
        }
    }

    override fun getLayout(): Int = R.layout.item_cleaning_popup

}