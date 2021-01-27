package com.illuminz.application.di

import android.app.Application
import android.content.Context
import com.illuminz.application.HotelApplication
import dagger.Module
import dagger.Provides

@Module
object ContextModule {
    @Provides
    @JvmStatic
    internal fun provideContext(application: HotelApplication): Context =
        application.applicationContext

    @Provides
    @JvmStatic
    internal fun provideApplication(application: HotelApplication): Application = application
}