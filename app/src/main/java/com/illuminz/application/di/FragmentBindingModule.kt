package com.illuminz.application.di

import com.illuminz.application.ui.custom.ConfirmDialog
import com.illuminz.application.ui.home.HomeFragment
import com.illuminz.application.ui.bar.DrinksFragment
import com.illuminz.application.ui.bar.items.DrinkDialog
import com.illuminz.application.ui.bookTable.BookTableFragment
import com.illuminz.application.ui.bookTable.BookingTimeFragment
import com.illuminz.application.ui.cart.FoodCartFragment
import com.illuminz.application.ui.cart.LaundryCartFragment
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.food.SearchFoodDialogFragment
import com.illuminz.application.ui.home.FeedbackFragment
import com.illuminz.application.ui.housekeeping.HouseKeepingFragment
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.laundry.LaundryListFragment
import com.illuminz.application.ui.laundry.SearchLaundryDialogFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.application.ui.massage.SearchMassageDialogFragment
import com.illuminz.application.ui.nearbyplaces.ImageDialogFragment
import com.illuminz.application.ui.nearbyplaces.NearbyFragment
import com.illuminz.application.ui.nearbyplaces.NearbyGalleryFragment
import com.illuminz.application.ui.roomcleaning.RoomCleaningFragment
import com.illuminz.application.ui.transport.TransportDialog
import com.illuminz.application.ui.transport.TransportFragment
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

    @ContributesAndroidInjector
    abstract fun transportFragment() : TransportFragment

    @ContributesAndroidInjector
    abstract fun feedbackFragment() : FeedbackFragment

    @ContributesAndroidInjector
    abstract fun foodCartFragment() : FoodCartFragment

    @ContributesAndroidInjector
    abstract fun laundryCartFragment() : LaundryCartFragment

    @ContributesAndroidInjector
    abstract fun nearbyGalleryFragment() : NearbyGalleryFragment

    @ContributesAndroidInjector
    abstract fun houseKeepingFragment() : HouseKeepingFragment

    @ContributesAndroidInjector
    abstract fun restaurantDetailsSearchDialogFragment() : SearchFoodDialogFragment

    @ContributesAndroidInjector
    abstract fun searchLaundryDialogFragment() : SearchLaundryDialogFragment

    @ContributesAndroidInjector
    abstract fun searchMassageDialogFragment() : SearchMassageDialogFragment
}