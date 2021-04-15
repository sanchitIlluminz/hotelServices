package com.illuminz.application.ui.bookTable.items

import androidx.core.content.ContextCompat
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_booking_date.view.*

class BookingDateItem(
    val date: String,
    val day: String,
    var selected: Boolean = false
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvDay.text = day
            tvDate.text = date

            if (selected) {
                tvDate.setBackgroundResource(R.drawable.background_round_blue_date)
                tvDate.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                tvDate.setBackgroundResource(R.color.transparent)
                tvDate.setTextColor(ContextCompat.getColor(context, R.color.calendar_date_color))
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_booking_date
}