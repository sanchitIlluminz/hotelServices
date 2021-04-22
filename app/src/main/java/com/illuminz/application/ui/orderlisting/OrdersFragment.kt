package com.illuminz.application.ui.orderlisting

import android.os.Bundle
import android.view.View
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.google.android.material.tabs.TabLayoutMediator
import com.illuminz.application.R
import com.illuminz.data.models.response.FoodCartResponse
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : DaggerBaseFragment(), OrderClickListener {
    companion object {
        const val TAG = "OrdersFragment"
        fun newInstance() : OrdersFragment{
            val fragment = OrdersFragment()
            return fragment
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_orders

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initialise() {
        viewpager.adapter = OrderViewPagerAdapter(childFragmentManager,lifecycle)
        TabLayoutMediator(tabLayout,viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "Food"
                1 -> tab.text = "Laundry"
                else -> tab.text = "Others"
            }
        }.attach()
    }

    override fun openOrderDetail(orderResponse: FoodCartResponse) {
        if (parentFragmentManager.findFragmentByTag(OrderDetailFragment.TAG) == null) {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .replace(
                    R.id.fragmentContainer,
                    OrderDetailFragment.newInstance(orderResponse),
                    OrderDetailFragment.TAG
                )
                .addToBackStack(tag)
                .commit()
        }
    }
}