package com.illuminz.application.di

import com.illuminz.application.ui.cart.CartHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseBindingModule {
    @Provides
    @Singleton
    fun providesCartDetails(): CartHandler {
        return CartHandler()
    }
}