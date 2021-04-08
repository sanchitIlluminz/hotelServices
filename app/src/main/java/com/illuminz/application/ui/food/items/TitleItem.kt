package com.illuminz.application.ui.food.items

import com.core.extensions.gone
import com.core.extensions.visible
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_title.view.*

class TitleItem(
    val title: String,
    private val totalItemCount: String? = null,
    private val foodCategoryCount: Int? = 0
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            if (foodCategoryCount != 0) {
                tvTitle.text = title
                tvTitle.visible()
            } else {
                tvTitle.gone()
            }

            if (totalItemCount != null) {
                tvItemNumber.visible()
                tvItemNumber.text = totalItemCount
//                tvNoResults.gone()
            } else {
                tvItemNumber.gone()
//                tvNoResults.visible()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_title
}