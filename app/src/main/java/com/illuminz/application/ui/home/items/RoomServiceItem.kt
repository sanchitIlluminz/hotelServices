package com.illuminz.application.ui.home.items

import com.illuminz.application.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_room_service.view.*


class RoomServiceItem : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            tvBreakfastTime.text = "8am - 10 am"

            tvLunchTime.text = "1pm - 2:30pm"

            tvDinnerTime.text = "8pm - 11pm"

            tvReceptionNumber.text = "119"

            tvHotelNumber.text = "219"

            tvEmergencyNumber.text = "319"

            tvName.text = "EmpireWifi1"

            tvPassword.text = "ANM230v1"
        }

    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 6
    }

    override fun getLayout(): Int = R.layout.item_room_service
}