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
import com.illuminz.application.ui.custom.ErrorView
import com.illuminz.application.ui.laundry.items.LaundryItem
import com.illuminz.data.models.common.Status
import kotlinx.android.synthetic.main.fragment_laundry.*
import kotlinx.android.synthetic.main.fragment_laundry.toolbar

class LaundryFragment : DaggerBaseFragment(), SearchLaundryDialogFragment.Callback,
    CartBarView.Callback, ErrorView.ErrorButtonClickListener  {
    companion object {
        const val TAG = "LaundryFragment"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_CONNECTION_ERROR = 2
        private const val FLIPPER_CHILD_LOADING = 1

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
        // getLaundryDetails()
    }

    private fun initialise() {
        cartBarView.gone()

        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_service_TAG).orEmpty()

        if (viewModel.getLaundryObserver().value?.isSuccess() == true) {
            setBaiscData()
        } else {
            if (requireContext().isNetworkActiveWithMessage())
                viewModel.getLaundryDetails(serviceId, serviceTag)
        }
    }

    private fun setObservers() {
        viewModel.getAmountObserver().observe(viewLifecycleOwner, Observer { resource ->
            if (resource.status == Status.SUCCESS) {
                val quantity = resource.data?.first.orZero()
                if (quantity == 0) {
                    cartBarView.gone()
                } else {
                    cartBarView.visible()
                    val price = resource.data?.second.orZero()
                    cartBarView.setItemPrice(
                        totalPrice = price,
                        items = quantity
                    )
                }
            }
        })

        viewModel.getLaundryObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }
                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    setBaiscData()
                }
                Status.ERROR -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_CONNECTION_ERROR
                    handleError(resource.error)
                }
            }
        })
    }

    private fun setBaiscData() {
        if (!viewModel.savedCartEmptyOrNull()) {
            viewModel.updateOriginalList()
            viewModel.updateLaundryPrice()
        }
        viewpager.adapter = LaundryViewPagerAdapter(childFragmentManager, lifecycle, serviceId, serviceTag)

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.laundry_tab_title_ironing)
                else -> tab.text = getString(R.string.laundry_tab_title_wash_iron)
            }
        }.attach()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        ivSearch.setOnClickListener {
            val selectedTab = tabLayout.selectedTabPosition

            val laundryType = if (selectedTab == 0) {
                AppConstants.LAUNDRY_ONLY_IRON
            } else {
                AppConstants.LAUNDRY_WASH_IRON
            }

            val dialogFragment = SearchLaundryDialogFragment(this, laundryType)
            dialogFragment.show(childFragmentManager, SearchLaundryDialogFragment.TAG)
        }

        cartBarView.setCallback(this)

        connectionErrorView.setErrorButtonClickListener(this)
    }

//    private fun getLaundryDetails() {
//        if (isNetworkActiveWithMessage()) {
//            viewModel.getLaundryDetails(serviceId, serviceTag, -1)
//        }
//    }

    override fun onCartBarClick() {
//        val cartList = viewModel.getFinalCartList()
        val list = viewModel.getSavedCartList()
//
//        cartList.forEach { cartItem ->
//            if (cartItem.ironingPrice != null) {
//                val item = CartItemDetail(
//                    id = cartItem.id,
//                    type = cartItem.laundryType,
//                    quantity = cartItem.quantity
//                )
//                list.add(item)
//            }
//
//            if (cartItem.washIroningPrice != null) {
//                val item = CartItemDetail(
//                    id = cartItem.id,
//                    type = cartItem.laundryType,
//                    quantity = cartItem.quantity
//                )
//                list.add(item)
//            }
//        }
//
//        viewModel.addSavedCart(list)

        if (parentFragmentManager.findFragmentByTag(LaundryCartFragment.TAG) == null) {
            val fragment = LaundryCartFragment.newInstance(
                TAG
//                , list as ArrayList<CartItemDetail>?
            )
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onIncreaseSearchItemClicked(laundryItem: LaundryItem) {
        viewModel.updateOriginalLaundryList(laundryItem)
        val fragmentList = childFragmentManager.fragments
        fragmentList.forEach { fragment ->
            if (fragment is LaundryListFragment && fragment.getLaundryType() == laundryItem.laundryType) {
                fragment.updateLaundryAdapter(laundryItem)
            }
        }
    }

    override fun onDecreaseSearchItemClicked(laundryItem: LaundryItem) {
        viewModel.updateOriginalLaundryList(laundryItem)
        val fragmentList = childFragmentManager.fragments
        fragmentList.forEach { fragment ->
            if (fragment is LaundryListFragment && fragment.getLaundryType() == laundryItem.laundryType) {
                fragment.updateLaundryAdapter(laundryItem)
            }
        }
    }

    override fun onErrorButtonClicked() {
        if (requireContext().isNetworkActiveWithMessage())
            viewModel.getLaundryDetails(serviceId, serviceTag)
    }
}