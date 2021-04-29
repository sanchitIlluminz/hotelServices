package com.illuminz.application.ui.orderlisting.items

import android.content.Context
import androidx.core.content.ContextCompat
import com.core.utils.AppConstants
import com.google.android.material.textview.MaterialTextView
import com.illuminz.application.R
import com.illuminz.data.models.response.ServiceRequestDto
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_request_orders.view.*
import kotlinx.android.synthetic.main.item_request_orders.view.tvOrderStatus

class RequestOrderItem(
    private val serviceRequestDto: ServiceRequestDto
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            if (serviceRequestDto.requestType!=null){
                if (serviceRequestDto.requestType == AppConstants.SERVICE_REQUEST_TYPE_ROOM_CLEANING)
                    tvTitle.text = "Room Cleaning"

                else
                    tvTitle.text = "Cab Requested"
            }else{
                tvTitle.text = serviceRequestDto.packageName
            }

            tvOrderStatus.text = when(serviceRequestDto.status){
                AppConstants.ORDER_STATUS_NEW -> "Waiting"
                AppConstants.ORDER_STATUS_ACCEPTED -> "Accepted"
                AppConstants.ORDER_STATUS_READY_TO_DELIVER -> "Ready to deliver"
                AppConstants.ORDER_STATUS_DELIVERED -> "Delivered"
                AppConstants.ORDER_STATUS_CANCELLED -> "Cancelled"
                else -> "Picked"
            }

            formatOrderStatusText(serviceRequestDto.status,tvOrderStatus,context)

            tvSubTitle.text = serviceRequestDto.detail.orEmpty()
        }
    }

    override fun getLayout(): Int = R.layout.item_request_orders

    private fun formatOrderStatusText(text: Int?, textview: MaterialTextView, context: Context) {
        when(text){
            AppConstants.ORDER_STATUS_NEW,
            AppConstants.ORDER_STATUS_ACCEPTED -> {
                textview.setTextColor(ContextCompat.getColor(context,R.color.tab_highlight))
                textview.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_preparing_order,0,0,0)
            }
            AppConstants.ORDER_STATUS_READY_TO_DELIVER,
            AppConstants.ORDER_STATUS_DELIVERED -> {
                textview.setTextColor(ContextCompat.getColor(context,R.color.green))
                textview.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_delivered,0,0,0)
            }
            AppConstants.ORDER_STATUS_CANCELLED -> "Cancelled"
            else ->{
                textview.setTextColor(ContextCompat.getColor(context,R.color.grey_dark))
                textview.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_complete,0,0,0)
            }
        }
    }
}