package com.illuminz.application.ui.home.bookTable.items

import com.illuminz.application.R
import com.illuminz.application.utils.QuantityChangedPayload
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_booking_date.view.*
import kotlinx.android.synthetic.main.item_drink.view.*

class BookingDateItem(
    val date: String,
    val day: String,
    var selected: Boolean = false) : Item() {

//    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: MutableList<Any>) {
//        if (payloads.firstOrNull() == QuantityChangedPayload) {
//            if (selected){
//                tvDate.setBackgroundResource(R.drawable.background_round_blue_date)
//            }else{
//                tvDate.setBackgroundResource(R.color.colorPrimary)
//            }
//        } else {
//            super.bind(viewHolder, position, payloads)
//        }
//    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvDay.text = day
            tvDate.text = date

            if (selected){
                tvDate.setBackgroundResource(R.drawable.background_round_blue_date)
                tvDate.setTextColor(resources.getColor(R.color.white))

            }else{
                tvDate.setBackgroundResource(R.color.add_button_background_color)
                tvDate.setTextColor(resources.getColor(R.color.calendar_date_color))

            }
        }
    }

    override fun getLayout(): Int = R.layout.item_booking_date
}