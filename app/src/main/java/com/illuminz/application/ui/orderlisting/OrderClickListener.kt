package com.illuminz.application.ui.orderlisting

import com.illuminz.data.models.response.FoodCartResponse

interface OrderClickListener {
    fun openOrderDetail(orderResponse: FoodCartResponse, orderType: Int)
}