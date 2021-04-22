package com.illuminz.application.ui.housekeeping

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.core.ui.base.DaggerBaseFragment
import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.extensions.gone
import com.core.extensions.isNetworkActiveWithMessage
import com.core.extensions.visible
import com.illuminz.application.R
import com.illuminz.application.ui.housekeeping.items.HouseKeepingItem
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import kotlinx.android.synthetic.main.fragment_house_keeping.*
import javax.inject.Inject

class HouseKeepingFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "HouseKeepingFragment"

        private const val KEY_SERVICE_ID = "KEY_SERVICE_ID"
        private const val KEY_service_TAG = "KEY_TAG"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(serviceId: String, serviceTag: String): HouseKeepingFragment {
            val fragment = HouseKeepingFragment()
            val arguments = Bundle()
            arguments.putString(KEY_SERVICE_ID, serviceId)
            arguments.putString(KEY_service_TAG, serviceTag)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var serviceId: String
    private lateinit var serviceTag: String

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HouseKeepingViewModel::class.java]
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

        if (requireContext().isNetworkActiveWithMessage()) {
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
        tilHouseKeeping.gone()
    }

    private fun setListener() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter.setOnItemClickListener { item, view ->
            if (item is HouseKeepingItem) {

                if (item.title.equals("Others")) {
                    if (item.isChecked == true)
                        tilHouseKeeping.visible()
                    else
                        tilHouseKeeping.gone()
                }
            }
        }

        btConfirm.setOnClickListener {
            showConfirmationDialog(
                "Request submitted",
                ""
            )
        }
    }

    private fun setObservers() {
        viewModel.getHouseObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
//                    resource.data?.let { setBasicData(it)  }
                }

                Status.ERROR -> {
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

    private fun showConfirmationDialog(title: String, subtitle: String) {
        val dialog = context?.let { Dialog(it) }

        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            val contentView = View.inflate(requireContext(), R.layout.dialog_confirm, null)
            setContentView(contentView)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
            }

            contentView.tvTitle.text = title
            contentView.tvSubtitle.text = subtitle

            btnOkay.setOnClickListener {
                dismiss()
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            show()
        }
    }
}