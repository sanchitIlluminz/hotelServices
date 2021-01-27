package com.core.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

class FragmentSwitcher(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerResId: Int
) {
    private var selectedFragmentTag: String? = null

    /**
     * Checks whether or not fragment with provided tag exists in the fragment manager.
     * */
    fun fragmentExists(tag: String): Boolean {
        if (selectedFragmentTag == tag) {
            Timber.i("Selected fragment tag is same : $selectedFragmentTag")
            return true
        }

        val existingFragment = fragmentManager.findFragmentByTag(tag)

        if (existingFragment != null) {
            Timber.i("Fragment already exists : $tag")
            val transaction = fragmentManager.beginTransaction()
            transaction.show(existingFragment)

            if (selectedFragmentTag != null) {
                val selectedFragment = fragmentManager.findFragmentByTag(selectedFragmentTag)
                if (selectedFragment != null) {
                    transaction.hide(selectedFragment)
                }
            }

            transaction.commitAllowingStateLoss()
            selectedFragmentTag = tag
        }

        return existingFragment != null
    }

    fun addFragment(fragment: Fragment, tag: String) {
        if (selectedFragmentTag == tag) {
            Timber.i("Selected fragment tag is same as provided one : $tag")
            return
        } else {
            Timber.i("Adding fragment : $tag")
        }

        val transaction = fragmentManager.beginTransaction()
        transaction.add(containerResId, fragment, tag)

        if (selectedFragmentTag != null) {
            val selectedFragment = fragmentManager.findFragmentByTag(selectedFragmentTag)
            if (selectedFragment != null) {
                transaction.hide(selectedFragment)
            }
        }

        transaction.commitAllowingStateLoss()

        selectedFragmentTag = tag
    }

    fun setSelectedFragmentTag(tag: String?) {
        selectedFragmentTag = tag
    }

    fun getSelectedFragmentTag(): String? = selectedFragmentTag
}