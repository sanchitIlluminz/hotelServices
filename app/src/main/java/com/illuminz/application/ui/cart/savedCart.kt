package com.illuminz.application.ui.cart

import com.illuminz.data.models.request.CartRequest

data class savedCart (
    var type: String? = null,

    var items: List<CartRequest>? = null
)