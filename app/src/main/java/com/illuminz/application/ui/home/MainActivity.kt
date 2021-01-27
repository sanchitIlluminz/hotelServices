package com.illuminz.application.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.core.ui.base.DaggerBaseActivity
import com.illuminz.application.R
import com.illuminz.application.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, HomeFragment.newInstance(), HomeFragment.TAG).commit()



    }

}