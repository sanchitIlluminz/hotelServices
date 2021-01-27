package com.illuminz.application.ui.home.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_booking_details.view.*

class BookingDetailItem : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {

            tvGuestName.text = "Anmol Arora"

            tvStayDuration.text = "Stay: 3D/2N"

            tvRoomNumber.text = "Room #203"

            GuestNumber.text = resources.getQuantityString(R.plurals.guest_with_count, 4, 4)

            tvCheckInDate.text = "Fri, 18 Dec"

            tvCheckInTime.text = "10:00 AM"

            tvCheckOutDate.text = "Sat, 19 Dec"

            tvCheckOutTime.text = "11:00 AM"

        }
    }

    override fun getLayout(): Int = R.layout.item_booking_details

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 6
    }
}