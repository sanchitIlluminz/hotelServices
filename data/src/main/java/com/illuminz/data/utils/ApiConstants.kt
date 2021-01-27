package com.illuminz.data.utils

object ApiConstants {
    const val BOOKING_STATUS_CANCELLED = "cancelled"
    const val BOOKING_STATUS_RESCHEDULED = "rescheduled"
    const val BOOKING_STATUS_SUCCESS = "success"
    const val BOOKING_STATUS_PENDING = "pending"

    const val WALLET_TRANSACTION_CREDIT = "credit"
    const val WALLET_TRANSACTION_DEBIT = "debit"

    //search List
    const val SEARCH_LIST_CLASSES = "classes"
    const val SEARCH_LIST_TEACHERS = "teachers"
    const val SEARCH_LIST_CATEGORIES = "categories"
    const val SEARCH_LIST_TOPICS = "topics"


    //instant search deType
    const val INSTANT_CLASS_TYPE_POPULAR_RIGHT_NOW = "popular_right_now"
    const val INSTANT_CLASS_TYPE_INSTANT_CLASSES = "instant_classes"
    const val INSTANT_CLASS_TYPE_TRENDING_TOPICS = "trending_topics"

    //for Chat
    const val CHAT_TYPE_CLASS = "classes"
    const val CHAT_TYPE_PERSONAL = "personal"
    const val CHAT_TYPE_REQUEST = "requests"

    // schedule class sub type
    const val CLASS_SUB_TYPE_SINGLE = "single"
    const val CLASS_SUB_TYPE_COURSE = "course"

    // schedule class level
    const val CLASS_LEVEL_BEGINNER = "beginner"
    const val CLASS_LEVEL_INTERMEDIATE = "intermediate"
    const val CLASS_LEVEL_EXPERT = "expert"
}