package com.illuminz.application.ui.laundry

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.*
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.core.utils.AppConstants
import com.google.android.material.tabs.TabLayoutMediator
import com.illuminz.application.R
import com.illuminz.application.ui.cart.LaundryCartFragment
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.laundry.items.LaundryItem
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.request.CartItemDetail
import kotlinx.android.synthetic.main.fragment_laundry.*
import kotlinx.android.synthetic.main.fragment_laundry.toolbar

class LaundryFragment : DaggerBaseFragment(), SearchLaundryDialogFragment.Callback,
    CartBarView.Callback {
    companion object {
        const val TAG = "LaundryFragment"

        private const val KEY_SERVICE_ID = "KEY_SERVICE_ID"
        private const val KEY_service_TAG = "KEY_TAG"

        fun newInstance(serviceId: String, serviceTag: String): LaundryFragment {
            val fragment = LaundryFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID, serviceId)
            arguments.putString(KEY_service_TAG, serviceTag)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var serviceId: String
    private lateinit var serviceTag: String

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LaundryViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_laundry

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
        setObservers()
        setCart()
    }



    private fun initialise() {
        cartBarView.gone()

        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_service_TAG).orEmpty()

        viewpager.adapter = LaundryViewPager(childFragmentManager, lifecycle, serviceId, serviceTag)

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "Ironing"
                else -> tab.text = "Wash & Iron"
            }
        }.attach()
    }

    private fun setObservers() {
        viewModel.getAmountObserver().observe(viewLifecycleOwner, Observer { resource ->
            if (resource.status == Status.SUCCESS) {
                // Set visibilty and data of cartBar
                if (!resource?.data?.get(1).isNullOrZero()) {
                    cartBarView.visible()
                    resource.data.let {
                        cartBarView.setItemPrice(
                            totalPrice = it?.get(0).orZero(),
                            items = it?.get(1)?.toInt().orZero()
                        )
                    }
                } else {
                    cartBarView.gone()
                }
            }
        })
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        ivSearch.setOnClickListener {
            val selectedTab = tabLayout.selectedTabPosition

            val laundryType = if (selectedTab == 0) {
                AppConstants.LAUNDRY_ONLY_IRON
            } else {
                AppConstants.LAUNDRY_WASH_IRON
            }

            val dialogFragment = SearchLaundryDialogFragment(this, laundryType)
            dialogFragment.show(childFragmentManager, "")
        }

        cartBarView.setCallback(this)
    }

    private fun setCart() {
        val cartList = viewModel.getFinalCartList()
    }
    override fun onCartBarClick() {
        val cartList = viewModel.getFinalCartList()

        val list = arrayListOf<CartItemDetail>()
        for (i in 0 until cartList.size) {
            if (cartList[i].ironingPrice != null) {
                val item = CartItemDetail(
                    id = cartList[i].id,
                    type = cartList[i].laundryType,
                    quantity = cartList[i].quantity
                )
                list.add(item)
            }
            if (cartList[i].washIroningPrice != null) {
                val item = CartItemDetail(
                    id = cartList[i].id,
                    type = cartList[i].laundryType,
                    quantity = cartList[i].quantity
                )
                list.add(item)
            }

        }

        viewModel.addSavedCart(list)

        if (parentFragmentManager.findFragmentByTag(LaundryCartFragment.TAG) == null) {
            val fragment = LaundryCartFragment.newInstance(TAG, list)
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onIncreaseSearchItemClicked(laundryItem: LaundryItem) {
        val fragmentList = childFragmentManager.fragments
        fragmentList.forEach { fragment ->
            if (fragment is LaundryListFragment && fragment.getLaundryType() == laundryItem.laundryType) {
                fragment.updateLaundryAdapter(laundryItem)
            }

        }
    }

    override fun onDecreaseSearchItemClicked(laundryItem: LaundryItem) {
        val fragmentList = childFragmentManager.fragments
        fragmentList.forEach { fragment ->
            if (fragment is LaundryListFragment && fragment.getLaundryType() == laundryItem.laundryType) {
                fragment.updateLaundryAdapter(laundryItem)
            }
        }
    }
}