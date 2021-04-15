package com.illuminz.application.ui.bookTable.items

import androidx.core.content.ContextCompat
import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_booking_time.view.*
import java.util.*

class BookingTimeItem(
    val timeValue: Int,
    var selected: Boolean = false
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvTimeValue.text = String.format(Locale.ENGLISH, "%02d", timeValue)
            if (selected) {
                tvTimeValue.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                tvTimeValue.setTextColor(ContextCompat.getColor(context, R.color.text_color_3))
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_booking_time
}