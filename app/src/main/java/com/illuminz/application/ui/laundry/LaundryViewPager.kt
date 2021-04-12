package com.illuminz.application.ui.laundry

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.core.utils.AppConstants

class LaundryViewPager(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val serviceId:String,
    private val serviceTag:String
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LaundryListFragment.newInstance(AppConstants.LAUNDRY_ONLY_IRON,serviceId,serviceTag)
            else -> LaundryListFragment.newInstance(AppConstants.LAUNDRY_WASH_IRON,serviceId,serviceTag)
        }
    }
}
