package com.illuminz.application.ui.food.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_veg_non_veg_selection.view.*

class VegNonVegItem(
    var isVegOnly: Boolean = true,
    var isNonVegOnly: Boolean = false,
    val callback: Callback
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            cbVeg.isChecked = isVegOnly
            cbNonVeg.isChecked = isNonVegOnly

            cbVeg.setOnCheckedChangeListener { _, isChecked ->
                isVegOnly = isChecked
                callback.vegOnlyClickListener(isVegOnly, isNonVegOnly)
            }

            cbNonVeg.setOnCheckedChangeListener { _, isChecked ->
                isNonVegOnly =isChecked
                callback.vegOnlyClickListener(isVegOnly, isNonVegOnly)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_veg_non_veg_selection

    interface Callback{
       fun  vegOnlyClickListener(vegOnly: Boolean,nonVegOnly: Boolean)
    }
}