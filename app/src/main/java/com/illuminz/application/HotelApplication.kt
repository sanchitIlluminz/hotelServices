package com.illuminz.application

import android.os.StrictMode
import com.illuminz.application.di.DaggerAppComponent
import com.illuminz.application.utils.CrashlyticsTree
import com.illuminz.data.di.NetworkModule
import com.illuminz.data.utils.LocaleManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class HotelApplication : DaggerApplication() {

    private val applicationInjector = DaggerAppComponent.builder()
        .application(this)
        .networkModule(
            NetworkModule(
                BuildConfig.SERVER_BASE_URL,
                BuildConfig.SOCKET_BASE_URL,
                BuildConfig.DEBUG
            )
        )
        .build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector

    override fun onCreate() {
        // ThreeTenBP for times and dates, called before super to be available for objects
        setupThreeTen()
        // Enable strict mode before Dagger creates graph
        setupStrictMode()
        super.onCreate()
        setupTimber()
        setupLocaleManager()
        //setupCrashlytics()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }

    private fun setupThreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }

    private fun setupLocaleManager() {
        LocaleManager.init(this)
    }

/*    private fun setupCrashlytics() {
        val crashlytics = Crashlytics.Builder()
            .core(
                CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build()
            )
            .build()
        Fabric.with(this, crashlytics)
    }*/
}