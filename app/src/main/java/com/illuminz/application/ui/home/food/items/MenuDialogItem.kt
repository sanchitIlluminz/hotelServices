package com.illuminz.application.ui.home.food.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_dialog_menu.view.*

class MenuDialogItem(
    private val title: String,
    private val number: Int
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {
            tvMenuItem.text =  title
            tvMenuItemNumber.text = number.toString()
        }
    }

    override fun getLayout(): Int = R.layout.item_dialog_menu
}