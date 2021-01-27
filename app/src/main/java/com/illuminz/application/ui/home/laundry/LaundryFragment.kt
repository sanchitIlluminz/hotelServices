package com.illuminz.application.ui.home.laundry

import android.os.Bundle
import android.view.View
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.google.android.material.tabs.TabLayoutMediator
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.home.food.FoodCartFragment
import kotlinx.android.synthetic.main.fragment_laundry.*
import kotlinx.android.synthetic.main.fragment_laundry.toolbar

class LaundryFragment : DaggerBaseFragment(), CartBarView.Callback {


    companion object {
       const val TAG = "LaundryFragment"
        fun newInstance() : LaundryFragment{
            return LaundryFragment()
        }
    }
    private var totalPrice:Double = 0.00

    override fun getLayoutResId(): Int = R.layout.fragment_laundry

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewpager.adapter = LaundryViewPager(parentFragmentManager,lifecycle)

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "Ironing"
                else -> tab.text = "Wash & Iron"
            }
        }.attach()


        setData()
        setListeners()
    }

    private fun setData() {
        cartBarView.setButtonText(getString(R.string.view_cart))
        cartBarView.setItemPrice(totalPrice = 820.00,items = 4)

    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        cartBarView.setCallback(this)
    }

    override fun onCartBarClick() {
        if (parentFragmentManager.findFragmentByTag(FoodCartFragment.TAG)== null){
            val fragment = FoodCartFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .replace(R.id.fragmentContainer,fragment)
                .addToBackStack(TAG)
                .commit()
        }
    }
}