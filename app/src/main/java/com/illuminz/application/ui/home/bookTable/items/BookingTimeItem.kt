package com.illuminz.application.ui.home.bookTable.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_booking_time.view.*
import java.util.*

class BookingTimeItem(val timeValue: Int) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTimeValue.text = String.format(Locale.ENGLISH, "%02d", timeValue)
        }
    }

    override fun getLayout(): Int = R.layout.item_booking_time
}