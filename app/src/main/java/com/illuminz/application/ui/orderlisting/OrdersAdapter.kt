package com.illuminz.application.ui.orderlisting

import android.content.Context
import com.illuminz.application.ui.housekeeping.items.HouseKeepingItem
import com.illuminz.application.ui.orderlisting.items.LoadingItem
import com.illuminz.application.ui.orderlisting.items.OrderDetailItem
import com.illuminz.application.ui.orderlisting.items.OrderItem
import com.illuminz.data.models.response.SaveFoodOrderResponse
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import org.threeten.bp.ZonedDateTime
import timber.log.Timber

class OrdersAdapter(
    private var context: OrderItem.Callback
) : GroupAdapter<GroupieViewHolder>() {
    private val loadingItem by lazy { LoadingItem() }
    private var previousTransactionDate: ZonedDateTime? = null
    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            add(loadingItem)
        } else {
            try {
                remove(loadingItem)
            } catch (exception: Exception) {
                Timber.w(exception)
            }
        }
    }

    fun getLoadingPosition(): Int = getAdapterPosition(loadingItem)

    fun addItems(isFirstPage: Boolean, items: MutableList<SaveFoodOrderResponse>) {
        if (isFirstPage) {
            clear()
            //add(TransfersFilterItem(callBack))
        } else {
            setLoading(false)
        }


        val orderItems = mutableListOf<Item>()
        items.forEachIndexed { index, order ->
            val orderItemListAdapter = GroupAdapter<GroupieViewHolder>()
            order.orderDetail?.items?.forEach { cartItem ->
                orderItemListAdapter.add(OrderDetailItem(cartItem))
            }
            add(OrderItem(order,orderItemListAdapter,context))
//            orderItems.add(OrderItem(order))
        }
//        addAll(orderItems)
    }
//    interface CallBack : TransfersFilterItem.CallBack

}