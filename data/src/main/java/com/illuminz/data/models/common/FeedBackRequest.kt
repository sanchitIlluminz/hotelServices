package com.illuminz.data.models.common

data class FeedBackRequest(
    val name: String? = null,
    val email: String? = null,
    val areaCode: String? = null,
    val phoneNumber: String? = null,
    val subject: String? = null,
    val description: String? = null,
    val categoryId: String? = null,
    val documentIds: List<String>? = null
)