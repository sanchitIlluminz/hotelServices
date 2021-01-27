package com.illuminz.application.di

import com.illuminz.application.firebase.FcmService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBindingModule {
    @ContributesAndroidInjector
    abstract fun fcmService(): FcmService
}