package com.illuminz.application.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.di.ViewModelFactory
import com.core.di.ViewModelKey
import com.illuminz.application.ui.cart.FoodCartViewModel
import com.illuminz.application.ui.cart.LaundryCartViewModel
import com.illuminz.application.ui.food.FoodViewModel
import com.illuminz.application.ui.home.ServicesViewModel
import com.illuminz.application.ui.housekeeping.HouseKeepingViewModel
import com.illuminz.application.ui.laundry.LaundryViewModel
import com.illuminz.application.ui.massage.MassageViewModel
import com.illuminz.application.ui.nearbyplaces.NearbyViewModel
import com.illuminz.application.ui.orderlisting.OrderListingViewModel
import com.illuminz.application.ui.welcome.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun signUpViewModel(viewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ServicesViewModel::class)
    abstract fun servicesViewModel(viewModel: ServicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FoodViewModel::class)
    abstract fun foodViewModel(viewModel: FoodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LaundryViewModel::class)
    abstract fun laundryViewModel(viewModel: LaundryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MassageViewModel::class)
    abstract fun massageViewModel(viewModel: MassageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HouseKeepingViewModel::class)
    abstract fun houseKeepingViewModel(viewModel: HouseKeepingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NearbyViewModel::class)
    abstract fun nearbyViewModel(viewModel: NearbyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FoodCartViewModel::class)
    abstract fun foodCartViewModel(viewModelFood: FoodCartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LaundryCartViewModel::class)
    abstract fun laundryCartViewModel(viewModelFood: LaundryCartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderListingViewModel::class)
    abstract fun orderListingViewModel(viewModelFood: OrderListingViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}