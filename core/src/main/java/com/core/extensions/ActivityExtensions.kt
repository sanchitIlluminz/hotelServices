package com.core.extensions

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

fun AppCompatActivity.setTransparentStatusBar() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

fun FragmentActivity.disableBackPress(lifecycleOwner: LifecycleOwner) {
    onBackPressedDispatcher.addCallback(lifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Block back pressed
            }
        })
}