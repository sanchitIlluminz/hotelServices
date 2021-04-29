package com.illuminz.application.ui.nearbyplaces

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.core.extensions.isNetworkActiveWithMessage
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.nearbyplaces.items.GalleryDetailsItem
import com.illuminz.application.ui.nearbyplaces.items.GalleryImageFullWidthItem
import com.illuminz.application.ui.nearbyplaces.items.GalleryImageHalfWidthItem
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import kotlinx.android.synthetic.main.fragment_nearby_gallery.*

class NearbyGalleryFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "NearbyGalleryFragment"

        private const val KEY_DETAILS = "KEY_DETAILS"

        fun newInstance(serviceCategoryItem: ServiceCategoryItemDto): NearbyGalleryFragment {
            val fragment = NearbyGalleryFragment()
            val args = Bundle()
            args.putParcelable(KEY_DETAILS, serviceCategoryItem)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[NearbyViewModel::class.java]
    }

    private lateinit var serviceCategoryItem: ServiceCategoryItemDto

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun getLayoutResId(): Int = R.layout.fragment_nearby_gallery

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
        setObservers()
    }

    private fun initialise() {
        serviceCategoryItem = requireArguments().getParcelable(KEY_DETAILS)
            ?: throw IllegalArgumentException("Details key is required")

        adapter = GroupAdapter()
        rvNearbyGallery.adapter = adapter

        val layoutManager = rvNearbyGallery.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = adapter.spanSizeLookup

        adapter.add(GalleryDetailsItem(serviceCategoryItem = serviceCategoryItem))

        serviceCategoryItem.gallery?.forEachIndexed { index, gallery ->
            val item = if (index % 3 == 0) {
                GalleryImageFullWidthItem(image = gallery.thumbnailPath.orEmpty())
            } else {
                GalleryImageHalfWidthItem(image = gallery.thumbnailPath.orEmpty())
            }
            adapter.add(item)
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        btnRequest.setOnClickListener {
            if (requireContext().isNetworkActiveWithMessage()){
                val roomDetailHandler = viewModel.getRoomHandler()
                val roomNo = roomDetailHandler.roomDetails.roomNo
                val groupCode = roomDetailHandler.roomDetails.groupCode
                viewModel.submitCabRequest(
                    roomNumber = roomNo,
                    groupCode = groupCode,
                    destination = serviceCategoryItem.title.orEmpty()
                )
            }
        }
    }

    private fun setObservers() {
        viewModel.getCabObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showLoading()
                }

                Status.SUCCESS -> {
                    dismissLoading()
                    showConfirmationDialog(
                        "Thank you",
                        "we will call you in next 5 mins")
                }

                Status.ERROR -> {
                    dismissLoading()
                    handleError(resource.error)
                }
            }
        })
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