package com.illuminz.application.ui.nearbyplaces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.nearbyplaces.items.GalleryDetailsItem
import com.illuminz.application.ui.nearbyplaces.items.GalleryImageFullWidthItem
import com.illuminz.application.ui.nearbyplaces.items.GalleryImageHalfWidthItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_nearby_gallery.*

class NearbyGalleryFragment : DaggerBaseFragment() {

    companion object {
        const val TAG = "NearbyGalleryFragment"
        private const val KEY_DETAILS = "KEY_DETAILS"
        fun newInstance(image: ArrayList<String>): NearbyGalleryFragment {
            val fragment = NearbyGalleryFragment()
            val args = Bundle()
            args.putStringArrayList(KEY_DETAILS, image)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var adapter:GroupAdapter<GroupieViewHolder>
    private var imageList =  arrayListOf<String>()

    override fun getLayoutResId(): Int = R.layout.fragment_nearby_gallery


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        btnRequest.setOnClickListener {

        }
    }

    private fun initialise() {
        arguments?.let {
           imageList.addAll(it.getStringArrayList(KEY_DETAILS) as ArrayList<String>)
        }
//       arguments?.getStringArrayList(KEY_DETAILS) as ArrayList<String>

       adapter= GroupAdapter()
       rvNearbyGallery.adapter = adapter

       val layoutManager = rvNearbyGallery.layoutManager as GridLayoutManager
       layoutManager.spanSizeLookup = adapter.spanSizeLookup

        val item = GalleryDetailsItem(
            title = "Gate of India Mumbai",
            description = "The India Gate is a war memorial located astride the Rajpath, on the eastern edge of the \"ceremonial axis\" of New Delhi, formerly called Kingsway.",
            address = "Address: Rajpath Marg, India Gate, New Delhi, Delhi 110001",
            height1 = "Height: 42 m",
            opened = "Opened: 12 February 1931"
        )

       adapter.add(item)

       for(i in 0 until imageList.size){
           if (i%3==0){
               val item = GalleryImageFullWidthItem(image = imageList[i])
               adapter.add(item)
           }else{
               val item = GalleryImageHalfWidthItem(image = imageList[i])
               adapter.add(item)
           }
       }


    }
}