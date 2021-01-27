package com.illuminz.application.ui.home.nearbyplaces.items

import com.core.utils.GlideApp
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_image_dialog.view.*

class ImageDialogItem(
    val image: String
    ): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            GlideApp.with(this)
                .load(image)
                .centerCrop()
                .into(ivImage)
        }
    }

    override fun getLayout(): Int = R.layout.item_image_dialog
}