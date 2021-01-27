package com.core.extensions

import androidx.fragment.app.FragmentTransaction
import com.core.R
import com.core.utils.AnimationDirection

fun FragmentTransaction.setCustomAnimations(direction: AnimationDirection): FragmentTransaction {
    return when (direction) {
        AnimationDirection.End -> setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )

        AnimationDirection.Bottom -> setCustomAnimations(
            R.anim.slide_in_up,
            R.anim.no_animation,
            R.anim.no_animation,
            R.anim.slide_out_down
        )

        AnimationDirection.None -> this
    }
}