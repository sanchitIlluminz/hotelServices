package com.illuminz.application.ui.nearbyplaces.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_gallery_details.view.*

class GalleryDetailsItem(
    private val title:String,
    private val description:String,
    private val address:String,
    private val height1:String,
    private val opened:String
) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            tvTitle.text = title
            tvDescription.text = description
            tvAddress.text = address
            tvHeight.text = height1
            tvOpened.text = opened

        }
    }

    override fun getLayout(): Int = R.layout.item_gallery_details

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 2
    }
}