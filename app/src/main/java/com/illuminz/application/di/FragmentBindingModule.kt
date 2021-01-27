package com.illuminz.application.di

import com.illuminz.application.ui.custom.ConfirmDialog
import com.illuminz.application.ui.home.HomeFragment
import com.illuminz.application.ui.home.bar.DrinksFragment
import com.illuminz.application.ui.home.bar.items.DrinkDialog
import com.illuminz.application.ui.home.bookTable.BookTableFragment
import com.illuminz.application.ui.home.bookTable.BookingTimeFragment
import com.illuminz.application.ui.home.food.FoodCartFragment
import com.illuminz.application.ui.home.food.FoodListFragment
import com.illuminz.application.ui.home.laundry.LaundryFragment
import com.illuminz.application.ui.home.laundry.LaundryListFragment
import com.illuminz.application.ui.home.massage.MassageListFragment
import com.illuminz.application.ui.home.nearbyplaces.ImageDialogFragment
import com.illuminz.application.ui.home.nearbyplaces.NearbyFragment
import com.illuminz.application.ui.home.roomcleaning.RoomCleaningFragment
import com.illuminz.application.ui.home.transport.TransportDialog
import com.illuminz.application.ui.payment.PaymentMethodFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector
    abstract fun homeFragment() : HomeFragment

    @ContributesAndroidInjector
    abstract fun foodListFragment() : FoodListFragment

    @ContributesAndroidInjector
    abstract fun foodCartFragment() : FoodCartFragment

    @ContributesAndroidInjector
    abstract fun laundryFragment() : LaundryFragment

    @ContributesAndroidInjector
    abstract fun laundryListFragment() : LaundryListFragment

    @ContributesAndroidInjector
    abstract fun roomCleaningFragment() : RoomCleaningFragment

    @ContributesAndroidInjector
    abstract fun drinksFragment() : DrinksFragment

    @ContributesAndroidInjector
    abstract fun massageListFragment() : MassageListFragment

    @ContributesAndroidInjector
    abstract fun nearbyFragment() : NearbyFragment

    @ContributesAndroidInjector
    abstract fun bookingFragment() : BookingTimeFragment

    @ContributesAndroidInjector
    abstract fun bookTableFragment() : BookTableFragment

    @ContributesAndroidInjector
    abstract fun confirmDialog() : ConfirmDialog

    @ContributesAndroidInjector
    abstract fun imageDialogFragment() : ImageDialogFragment

    @ContributesAndroidInjector
    abstract fun drinkDialog() : DrinkDialog

    @ContributesAndroidInjector
    abstract fun transportDialog() : TransportDialog

    @ContributesAndroidInjector
    abstract fun paymentMethodFragment() : PaymentMethodFragment


}