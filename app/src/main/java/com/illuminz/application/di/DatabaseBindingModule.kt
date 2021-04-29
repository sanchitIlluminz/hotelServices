package com.illuminz.application.di

import com.illuminz.application.ui.cart.CartHandler
import com.illuminz.application.ui.home.RoomDetailsHandler
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

    @Provides
    @Singleton
    fun providesRoomDetails(): RoomDetailsHandler {
        return RoomDetailsHandler()
    }
}