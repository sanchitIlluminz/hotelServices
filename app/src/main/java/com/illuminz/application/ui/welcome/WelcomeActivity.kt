package com.illuminz.application.ui.welcome

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.core.utils.GlideApp
import com.illuminz.application.R
import com.illuminz.application.ui.custom.MyDeviceAdminReceiver
import com.illuminz.application.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager

    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_welcome)
        initialise()
        setBasicData()
        setListener()

//        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this)
//
//        try {
//            Runtime.getRuntime()
//                .exec("dpm set-device-owner com.illuminz.application.ui.custom/.MyDeviceAdminReceiver")
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//        }
//
//        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//
//        if (mDevicePolicyManager.isDeviceOwnerApp(packageName)) {
//            val a =10
////            setKioskPolicies()
//        } else {
//            val a =10
//
//        }
//
//
//
//        mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, arrayOf(packageName))
////        startLockTask()
//
//        val intentFilter = IntentFilter(Intent.ACTION_MAIN)
//        intentFilter.addCategory(Intent.CATEGORY_HOME)
//        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
//        mDevicePolicyManager.addPersistentPreferredActivity(mAdminComponentName,
//            intentFilter, ComponentName(packageName, WelcomeActivity::class.java.name)
//        )


//        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, true)

//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//
//
//        mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
//            Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
//            Integer.toString(
//                BatteryManager.BATTERY_PLUGGED_AC
//                    or BatteryManager.BATTERY_PLUGGED_USB
//                    or BatteryManager.BATTERY_PLUGGED_WIRELESS))
//
//        window.decorView.systemUiVisibility = flags


    }

    private fun setListener() {
        btnExplore.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            finish()
        }
    }

    private fun setBasicData() {
        tvName.text = getString(R.string.hi_monica)
        tvMessage.text = "Have a nice stay at Empire Hotels"
    }


    private fun initialise() {
        GlideApp.with(this)
            .load(R.drawable.ic_imagemain)
            .placeholder(R.color.colorPrimary)
            .error(R.color.black)
            .centerCrop()
            .into(ivImage)
    }
}