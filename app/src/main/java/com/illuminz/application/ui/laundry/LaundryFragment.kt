package com.illuminz.application.ui.laundry

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.gone
import com.core.extensions.isNullOrZero
import com.core.extensions.orZero
import com.core.extensions.visible
import com.core.ui.base.DaggerBaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.data.models.common.Status
import kotlinx.android.synthetic.main.fragment_laundry.*
import kotlinx.android.synthetic.main.fragment_laundry.toolbar

class LaundryFragment : DaggerBaseFragment(), CartBarView.Callback {


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

    private var totalPrice: Double = 0.00

    private lateinit var serviceId: String
    private lateinit var serviceTag: String

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LaundryViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_laundry

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
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


        setData()
        setListeners()
        setObservers()
    }

    private fun setObservers() {

        viewModel.getPriceObserver().observe(viewLifecycleOwner, Observer { resource ->
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


//        viewModel.getCartObserver().observe(viewLifecycleOwner, Observer { resource ->
//            if (resource.isNotEmpty()){
//                cartBarView.visible()
//                var itemCount = 0
//                var totalPrice = 0.0
//                // Calculate total price and item count
//                resource.forEach { serviceCategory ->
//                    itemCount += serviceCategory.quantity
//                    totalPrice += (serviceCategory.price.orZero() * serviceCategory.quantity)
//                }
//
////            Set visibilty and data of cartBar
//                if (resource.isNotEmpty()) {
//                    cartBarView.visible()
//                    cartBarView.setItemPrice(totalPrice = totalPrice, items = itemCount)
//                } else {
//                    cartBarView.gone()
//                }
//            }else{
//                cartBarView.gone()
//            }
//
//        })
    }

    private fun setData() {
//        cartBarView.setButtonText(getString(R.string.view_cart))
//        cartBarView.setItemPrice(totalPrice = 820.00, items = 4)

    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        cartBarView.setCallback(this)
    }


    override fun onCartBarClick() {
//        if (parentFragmentManager.findFragmentByTag(CartFragment.TAG)== null){
//            val fragment = CartFragment.newInstance(TAG)
//            parentFragmentManager.beginTransaction()
//                .setCustomAnimations(AnimationDirection.End)
//                .add(R.id.fragmentContainer,fragment)
//                .addToBackStack(CartFragment.TAG)
//                .commit()
//        }
    }
}