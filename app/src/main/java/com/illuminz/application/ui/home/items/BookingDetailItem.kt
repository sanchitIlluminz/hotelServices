package com.illuminz.application.ui.home.items

import com.core.extensions.orZero
import com.illuminz.application.R
import com.illuminz.data.models.response.UserInfoDto
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_booking_details.view.*
import org.threeten.bp.ZonedDateTime

class BookingDetailItem(
    var callback:Callback,
    var userInfoDto: UserInfoDto
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            userInfoDto.apply {
                tvGuestName.text = guestName.orEmpty()

                val day = noOfNights.orZero()+1

                tvStayDuration.text = "Stay: ${day}D/${noOfNights}N"

                tvRoomNumber.text = "Room #203"

                GuestNumber.text = resources.getQuantityString(R.plurals.guest_with_count, 4, 4)

                tvCheckInDate.text = "Fri, 18 Dec"

                tvCheckInTime.text = "10:00 AM"

                tvCheckOutDate.text = "Sat, 19 Dec"

                tvCheckOutTime.text = "11:00 AM"

                tvExtendStay.setOnClickListener {
                    callback.onExtendStayClicked()
                }
            }


        }
    }

    override fun getLayout(): Int = R.layout.item_booking_details

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return 6
    }

    interface Callback {
        fun onExtendStayClicked()
    }
}