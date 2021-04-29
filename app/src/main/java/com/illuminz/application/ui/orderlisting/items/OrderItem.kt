package com.illuminz.application.ui.orderlisting.items

import android.content.Context
import androidx.core.content.ContextCompat
import com.core.extensions.orZero
import com.core.extensions.visible
import com.core.utils.AppConstants
import com.google.android.material.textview.MaterialTextView
import com.illuminz.application.R
import com.illuminz.data.models.response.SaveFoodOrderResponse
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_orders.view.*

class OrderItem(
    var orderResponse: SaveFoodOrderResponse? = null,
    private val adapter: GroupAdapter<GroupieViewHolder>,
    var callback:Callback
):Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val itemCount = orderResponse?.orderDetail?.items?.size?.orZero()
            val time = orderResponse?.updatedAt?.toLocalTime()

            tvItemCount.text = resources.getQuantityString(R.plurals.cart_items,itemCount.orZero(),itemCount)
            tvTime.text = time.toString()

            var totalAmount = 0.0
            orderResponse?.orderDetail?.items?.forEach { cartItem ->
                totalAmount += cartItem.cartTotal.orZero()
            }

            tvPrice.text = CurrencyFormatter.format(amount = totalAmount)
            rvOrderItem.adapter = adapter

            tvOrderStatus.text = when(orderResponse?.status){
                AppConstants.ORDER_STATUS_NEW -> "Waiting"
                AppConstants.ORDER_STATUS_ACCEPTED -> "Accepted"
                AppConstants.ORDER_STATUS_READY_TO_DELIVER -> "Ready to deliver"
                AppConstants.ORDER_STATUS_DELIVERED -> "Delivered"
                AppConstants.ORDER_STATUS_CANCELLED -> "Cancelled"
                else -> "Picked"
            }

            formatOrderStatusText(orderResponse?.status,tvOrderStatus,context)

            if(orderResponse?.status == AppConstants.ORDER_STATUS_ACCEPTED){
                tvPreparationTime.visible()
                tvPreparationTime.text = "ETA ${orderResponse?.requestedTime} Min"
            }
            else
                tvPreparationTime.onFocusChangeListener

            btnDetails.setOnClickListener {
                callback.onViewDetailsClicked(orderResponse)
            }
        }
    }

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

    override fun getLayout(): Int = R.layout.item_orders

    interface Callback{
        fun onViewDetailsClicked(orderResponse: SaveFoodOrderResponse?)
    }
}