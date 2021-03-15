package com.illuminz.application.ui.home.items

import com.core.utils.GlideApp
import com.illuminz.application.R
import com.illuminz.data.models.response.ServiceDto
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_services.view.*

class ServicesItem(
    val serviceDto: ServiceDto
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            GlideApp.with(this)
                .load(serviceDto.imagePath.orEmpty())
                .placeholder(R.color.colorPrimary)
                .error(R.color.black)
                .centerCrop()
                .into(ivImage)

            tvServiceName.text = serviceDto.title.orEmpty()
            tvDescription.text = serviceDto.detail.orEmpty()
        }
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 3
    }

    override fun getLayout(): Int = R.layout.item_services
}