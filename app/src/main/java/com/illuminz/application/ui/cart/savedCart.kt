package com.illuminz.application.ui.cart

import com.illuminz.data.models.request.CartItemDetail

data class savedCart (
    var items: List<CartItemDetail>? = null
)