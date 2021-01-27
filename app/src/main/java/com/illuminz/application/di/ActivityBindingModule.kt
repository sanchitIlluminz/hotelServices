package com.illuminz.application.di

import com.illuminz.application.ui.home.MainActivity
import com.illuminz.application.ui.welcome.WelcomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
//    @ContributesAndroidInjector
//    abstract fun welcomeActivity() : WelcomeActivity

    @ContributesAndroidInjector
    abstract fun mainActivity() : MainActivity
}