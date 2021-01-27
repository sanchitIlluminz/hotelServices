package com.illuminz.application.di

import com.illuminz.error.ErrorHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ErrorModule {
    @Provides
    @Singleton
    @JvmStatic
    internal fun providesErrorHandler(errorHandler: ErrorHandlerImpl): ErrorHandler {
        return errorHandler
    }
}