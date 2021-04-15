package com.illuminz.application.ui.orderlisting

import android.content.Context
import com.core.extensions.isSameDay
import com.illuminz.application.ui.food.items.FoodItem
import com.illuminz.application.ui.housekeeping.items.HouseKeepingItem
import com.illuminz.data.models.response.FoodCartResponse
import com.illuminz.data.models.response.SaveFoodOrderResponse
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import org.threeten.bp.ZonedDateTime
import timber.log.Timber

class OrdersAdapter() : GroupAdapter<GroupieViewHolder>() {
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
        items.forEachIndexed { index, serviceCategoryItem ->
                orderItems.add(HouseKeepingItem(title = serviceCategoryItem._id))

        }
        addAll(orderItems)
    }



//    interface CallBack : TransfersFilterItem.CallBack
}