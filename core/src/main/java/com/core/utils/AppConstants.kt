package com.core.utils

object AppConstants {
    /* const val URL_PRIVACY_POLICY = "https://www.gigipo.com/privacy-policy.html?app=1"
     const val URL_TERMS_AND_CONDITIONS = "https://www.gigipo.com/terms.html?app=1"
     const val URL_PAYMENT_TERMS = "https://www.gigipo.com/payment-terms.html?app=1" */
    const val DIGITAL_ID_KYC_URL = "http://kkbits.illuminz.com/appkyc-verification?token="
    const val URL_PRIVACY_POLICY = "http://kkbits.illuminz.com/privacy-policy?app=1"
    const val URL_TERMS_AND_CONDITIONS = "http://kkbits.illuminz.com/terms-condition?app=1"
    const val URL_PAYMENT_TERMS = "http://kkbits.illuminz.com/payment-terms?app=1"
    const val URL_ABOUT_US = "http://kkbits.illuminz.com/about-us?app=1"
    const val URL_FAQS = "https://dev.gigipo.com/faqs.html?app=1"
    const val URL_CANCELLATION_POLICY = "https://dev.gigipo.com/cancellation-policy.html?app=1"
    const val URL_HELP_AND_SUPPORT = "http://kkbits.illuminz.com/faq?app=1"
    const val TOLL_FREE = "1800 889 1803"
    const val CONTACT_US_EMAIL = "contact@gigipo.com"
    const val OTHER_ID = "Other"
    const val ZERO_TO_ONE = "ZERO_TO_ONE"
    const val ONE_TO_THREE = "ONE_TO_THREE"
    const val THREE_TO_FIVE = "THREE_TO_FIVE"
    const val MORE_THAN_FIVE = "MORE_THAN_FIVE"
    const val TEACHER_STEP = "TEACHER_STEP"
    const val MERCHANT_ID = "7001679"
    const val MERCHANT_KEY = "GXXgLuSS"
    const val CHECK = "check"

    /* const val MERCHANT_ID = "5001564"
     const val MERCHANT_KEY = "jqXIg0Ef"*/
    const val ENVIRONMENT_DEBUG = true
    const val ENVIRONMENT_LIVE = false
    const val BOOKING_STATUS_CANCELLED: String = "cancelled"
    const val BOOKING_STATUS_RESCHEDULED: String = "rescheduled"
    const val BOOKING_STATUS_SUCCESS: String = "success"
    const val BOOKING_STATUS_PENDING: String = "pending"
    const val BOOKING_STATUS_UPCOMING: String = "upcoming"
    const val TEACHER_FOLLOWING_STATUS: String = "followingStatus"
    const val TOPIC_FOLLOWING_STATUS: String = "followingStatus"
    const val SOCIAL_TYPE_FACEBOOK: String = "facebook"
    const val SOCIAL_TYPE_GOOGLE: String = "google"
    const val DATE_FORMAT: String = "dd MMMM,yyyy"
    const val EXTRA_CLASS_DATA: String = "classData"
    const val EXTRA_FROM_DRAFT: String = "from_draft"
    const val CLASS_TYPE_INSTANT: String = "instant"
    const val CLASS_TYPE_INDIVIDUAL: String = "individual"
    const val CLASS_TYPE_GROUP: String = "group"

    /* const val PAYMENT_MODE_CARD: String = "card"
     const val PAYMENT_MODE_UPI: String = "upi"
     const val PAYMENT_MODE_WALLET: String = "wallet"
     const val PAYMENT_MODE_OTHER: String = "other"*/

    fun getPrivacyPolicyUrl(languageCode: String): String {
        return "$URL_PRIVACY_POLICY&language=$languageCode"
    }

    fun getTermsConditionsUrl(languageCode: String): String {
        return "$URL_TERMS_AND_CONDITIONS&language=$languageCode"
    }

    const val REQUEST_CODE_GALLERY_IMAGE = 100
    const val REQUEST_CODE_GALLERY_VIDEO = 101
    const val REQUEST_CODE_CAMERA_IMAGE = 102
    const val REQUEST_CODE_CAMERA_VIDEO = 103
    const val REQUEST_CODE_DOCUMENT = 121
    const val REQUEST_CODE_START_KYC = 104

    //for transfers status
    const val INITIATED = 1
    const val COMPLETED = 2
    const val FAILED = 3
    const val CANCELLED = 4
    const val UNKNOWN = 5
    const val RECEIPT_UNVERIFIED = 6

    //for wallet status
    const val ALL = "all"
    const val PAID = "paid"
    const val RECEIVED = "received"

}