package com.illuminz.application.di

import com.illuminz.application.HotelApplication
import com.illuminz.data.di.NetworkModule
import com.illuminz.data.di.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [ActivityBindingModule::class,
        FragmentBindingModule::class,
        ServiceBindingModule::class,
        ViewModelModule::class,
        ContextModule::class,
        RepositoryModule::class,
        ErrorModule::class,
        DatabaseBindingModule::class,
        AndroidSupportInjectionModule::class]
)
@Singleton
interface AppComponent : AndroidInjector<HotelApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: HotelApplication): Builder

        fun networkModule(networkModule: NetworkModule): Builder

        fun build(): AppComponent
    }
}