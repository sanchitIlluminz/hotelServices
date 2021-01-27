package com.illuminz.application.ui.home.items

import com.bumptech.glide.Glide
import com.core.utils.GlideApp
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_more_services.view.*

class MoreServicesItem(
    private val image: String,
    private val name: String,
    private val description:String,
    private val type: Int,
    val tag: String
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            GlideApp.with(this)
                .load(image)
                .placeholder(R.color.colorPrimary)
                .error(R.color.black)
                .centerCrop()
                .into(ivImage)

            tvServiceName.text = name
            tvDescription.text = description
        }

    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 2
    }

    override fun getLayout(): Int = R.layout.item_more_services
}