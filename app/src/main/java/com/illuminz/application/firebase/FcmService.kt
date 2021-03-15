package com.illuminz.application.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.illuminz.data.repository.CommonRepository
import com.illuminz.data.repository.UserRepository
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class FcmService : FirebaseMessagingService() {
    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var commonRepository: CommonRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM Token Updated: $token")
//        if (userRepository.isLoggedIn()) {
//            //  GlobalScope.launch { commonRepository.updateFcmToken(token) }
//        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("Message data : ${message.data}")
        Timber.d("Message notification : ${message.notification?.title}, ${message.notification?.body}")
        if (message.data.isNotEmpty()) {

        }
    }
}