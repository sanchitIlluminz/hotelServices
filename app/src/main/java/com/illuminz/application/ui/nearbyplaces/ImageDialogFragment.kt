package com.illuminz.application.ui.nearbyplaces

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.core.ui.base.DaggerBaseDialogFragment
import com.illuminz.application.R
import com.illuminz.application.ui.nearbyplaces.items.ImageDialogItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_image.*
import java.util.*


class ImageDialogFragment: DaggerBaseDialogFragment() {

    companion object{
        const val TAG ="ImageDialogFragment"

        private const val  KEY_TITLE= "KEY_TITLE"
        private const val  KEY_SUBTITLE= "KEY_SUBTITLE"
        private const val  KEY_LIST_IMAGE= "KEY_LIST_IMAGE"

         fun newInstance(title: String, subtitle: String, imageList: ArrayList<String>): ImageDialogFragment {

             val args = Bundle()

             args.putString(KEY_TITLE, title)
             args.putString(KEY_SUBTITLE, subtitle)
             args.putStringArrayList(KEY_LIST_IMAGE, imageList)

             val fragment = ImageDialogFragment()
             fragment.arguments = args
             return fragment

         }

    }

    private lateinit var imageAdapter: GroupAdapter<GroupieViewHolder>

    override fun getLayoutResId(): Int = R.layout.dialog_image

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)

        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBasicData()
        setListeners()

    }

    private fun setListeners() {
    }

    private fun setBasicData() {
        val imageList = arguments?.getStringArrayList(KEY_LIST_IMAGE)

        tvTitle.text = arguments?.getString(KEY_TITLE)
        tvSubTitle.text = arguments?.getString(KEY_SUBTITLE)

        imageAdapter = GroupAdapter()
        rvDialog.adapter = imageAdapter



        imageList?.forEach { image->
            val imageLink = image

            val item = ImageDialogItem(imageLink)
            imageAdapter.add(item)
        }
    }
}