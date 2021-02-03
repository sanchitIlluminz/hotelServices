package com.illuminz.application.ui.home.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactDialogItem(
    private val title:String,
    private val value:String
) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTitle.text = title
            tvValue.text = value
        }
    }

    override fun getLayout(): Int = R.layout.item_contact
}