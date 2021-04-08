package com.illuminz.application.ui.housekeeping.items

import com.core.extensions.orFalse
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_house_keeping.view.*

class HouseKeepingItem(
    var title:String? = null,
    var isChecked: Boolean? = false
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            cbHouseKeeping.text = title.orEmpty()
            cbHouseKeeping.isChecked = isChecked.orFalse()

            cbHouseKeeping.setOnCheckedChangeListener{ _, checked ->
                isChecked = checked
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_house_keeping
}