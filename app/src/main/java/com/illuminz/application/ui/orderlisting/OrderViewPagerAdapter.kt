package com.illuminz.application.ui.orderlisting

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.core.utils.AppConstants

class OrderViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderListingFragment.newInstance(AppConstants.ORDER_TYPE_FOOD)
            1 -> OrderListingFragment.newInstance(AppConstants.ORDER_TYPE_LAUNDRY)
            else -> OrderListingFragment.newInstance(AppConstants.ORDER_TYPE_OTHERS)
        }
    }
}