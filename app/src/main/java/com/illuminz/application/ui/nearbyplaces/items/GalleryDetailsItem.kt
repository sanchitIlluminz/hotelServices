package com.illuminz.application.ui.nearbyplaces.items

import com.illuminz.application.R
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_gallery_details.view.*

class GalleryDetailsItem(
    private val serviceCategoryItem: ServiceCategoryItemDto
) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            tvTitle.text = serviceCategoryItem.title
            tvDescription.text = serviceCategoryItem.description
//            tvAddress.text = address
//            tvHeight.text = height1
//            tvOpened.text = opened

        }
    }

    override fun getLayout(): Int = R.layout.item_gallery_details

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 2
    }
}