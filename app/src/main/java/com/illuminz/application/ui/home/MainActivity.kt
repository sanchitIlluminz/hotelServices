package com.illuminz.application.ui.home

import android.os.Bundle
import android.view.WindowManager
import com.core.ui.base.DaggerBaseActivity
import com.illuminz.application.R

class MainActivity : DaggerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, HomeFragment.newInstance(), HomeFragment.TAG).commit()
    }
}