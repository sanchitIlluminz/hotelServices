package com.illuminz.application.ui.nearbyplaces.items
import com.core.utils.GlideApp
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_gallery_image_full_width.view.*

class GalleryImageFullWidthItem(
    private val image: String
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            GlideApp.with(context)
                .load(image)
                .into(ivImage)
        }
    }

    override fun getLayout(): Int = R.layout.item_gallery_image_full_width

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 2
    }
}