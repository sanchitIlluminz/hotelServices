package com.illuminz.application.ui.housekeeping

import com.core.ui.base.DaggerBaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.isNetworkActiveWithMessage
import com.illuminz.application.R
import com.illuminz.application.ui.housekeeping.items.HouseKeepingItem
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_house_keeping.*
import javax.inject.Inject

class HouseKeepingFragment : DaggerBaseFragment() {
    companion object{
        const val TAG = "HouseKeepingFragment"

        private const val KEY_SERVICE_ID = "KEY_SERVICE_ID"
        private const val KEY_service_TAG = "KEY_TAG"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(serviceId: String, serviceTag: String): HouseKeepingFragment{
            val fragment = HouseKeepingFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID, serviceId)
            arguments.putString(KEY_service_TAG, serviceTag)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var serviceId :String
    private lateinit var serviceTag :String

    private lateinit var adapter:GroupAdapter<GroupieViewHolder>

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory) [HouseKeepingViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_house_keeping

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setObservers()
        setListener()
    }

    private fun initialise() {
        serviceId = requireArguments().getString(KEY_SERVICE_ID).orEmpty()
        serviceTag = requireArguments().getString(KEY_service_TAG).orEmpty()

        adapter = GroupAdapter()
        rvHouseKeeping.adapter = adapter

        if (requireContext().isNetworkActiveWithMessage()){
            viewModel.getHouseKeeping(serviceId, serviceTag)
        }

        val item = listOf(
            HouseKeepingItem(title = "Dental Kit"),
            HouseKeepingItem(title = "Shower Kit"),
            HouseKeepingItem(title = "Shaving Kit"),
            HouseKeepingItem(title = "Room Cleaning"),
            HouseKeepingItem(title = "Others")
        )
        adapter.addAll(item)
    }

    private fun setListener() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setObservers() {
        viewModel.getHouseObserver().observe(viewLifecycleOwner, Observer { resource ->
            when(resource.status){
                Status.LOADING ->{
                   viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS ->{
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
//                    resource.data?.let { setBasicData(it)  }
                }

                Status.ERROR ->{
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                }
            }
        })
    }

    private fun setBasicData(list: List<ServiceCategoryItemDto>) {
//        list.forEachIndexed{index, serviceCategoryItem ->
//            adapter.add(
//                HouseKeepingItem(title = serviceCategoryItem.title)
//            )
//        }
    }
}