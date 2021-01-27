package com.illuminz.application.ui.home.nearbyplaces

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.custom.ConfirmDialog
import com.illuminz.application.ui.home.nearbyplaces.items.NearbyItem
import com.illuminz.application.ui.home.nearbyplaces.items.NearbySubTitleItem
import com.illuminz.application.ui.home.nearbyplaces.items.NearbyTitleItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_nearby.*

class NearbyFragment : DaggerBaseFragment() {

    companion object {
        const val TAG ="NearbyFragment"

        private const val  KEY_TITLE= "KEY_TITLE"
        private const val  KEY_SUBTITLE= "KEY_SUBTITLE"
        private const val  KEY_LIST_IMAGE= "KEY_LIST_IMAGE"

        fun newInstance(title: String, subtitle: String, imageList: java.util.ArrayList<String>): NearbyFragment{

            val args = Bundle()

            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subtitle)
            args.putStringArrayList(KEY_LIST_IMAGE, imageList)

            val fragment = NearbyFragment()
            fragment.arguments = args
            return fragment

        }
    }

    private lateinit var  adapter: GroupAdapter<GroupieViewHolder>
    private lateinit var imageList:ArrayList<String>

    override fun getLayoutResId(): Int = R.layout.fragment_nearby

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        adapter.setOnItemClickListener { item, view ->
            if (item is NearbyItem){
                val title = item.title

                ImageDialogFragment.newInstance(title,getString(R.string.ten_km_away),imageList)
                    .show(childFragmentManager,ImageDialogFragment.TAG)

//                ConfirmDialog.newInstance(getString(R.string.order_placed),
//                    getString(R.string.order_will_be_delivered_in_time))
//                    .show(childFragmentManager, ConfirmDialog.TAG)
            }
        }
    }

    private fun initialise() {
        adapter = GroupAdapter()
        rvNearby.adapter = adapter

        val layoutManager = rvNearby.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = adapter.spanSizeLookup

        val item1 = arguments?.getString(KEY_TITLE)?.let { NearbyTitleItem(title = it) }

        val item2 = arguments?.getString(KEY_SUBTITLE)?.let { NearbySubTitleItem(subTitle = it) }

        val item3 = listOf(
            NearbyItem(image = "https://2.bp.blogspot.com/-kIlA_BjfIqQ/T-CQ8WuaC7I/AAAAAAAAAkE/dTo_8UZxBBU/s1600/Mumbai+322.JPG",
                        title = "Gate of India Mumbai"),
            NearbyItem(image = "https://www.visittnt.com/blog/wp-content/uploads/2018/05/Marine-drive.jpg",
                        title = "Marine Drive"),
            NearbyItem(image = "https://lp-cms-production.imgix.net/2019-06/99ad019a84817e4bb5248d47c70dd1d2-chhatrapati-shivaji-terminus.jpg",
                        title = "Chhatrapati Shivaji Maharaj Vast…"),
            NearbyItem(image = "https://static.toiimg.com/photo/61984337.cms",
                        title = "Chatrapati Shivaji Termi…"),
            NearbyItem(image = "https://www.fabhotels.com/blog/wp-content/uploads/2019/06/Haji-Ali-Dargah_600-1280x720.jpg",
                        title = "Haji Ali Dargah"),
            NearbyItem(image = "https://upload.wikimedia.org/wikipedia/commons/6/62/Mumbai_Dhobi_Ghat_Laundry_District.JPG",
                        title = "Dhobi Ghat, Mumbai")
        )



        if (item1 != null) {
            adapter.add(item1)
        }
        if (item2 != null) {
            adapter.add(item2)
        }
        arguments?.getStringArrayList(KEY_LIST_IMAGE)?.let {
            it.forEach { item ->
                val nearbyItem = NearbyItem(image = item, title = "title")
                adapter.add(nearbyItem)
            }
        }
//        adapter.addAll(item3)


        imageList = arrayListOf("https://blog.architizer.com/wp-content/uploads/Heydar-ALiyev-Center-in-Baku_cropped.jpg",
                                "https://www.touropia.com/gfx/d/best-places-to-visit-in-india/amritsar.jpg?v=1",
                                "https://www.planetware.com/photos-large/IND/india-top-attractions-jaisalmer.jpg",
                                "https://www.planetware.com/wpimages/2019/11/india-best-places-to-visit-agra.jpg",
                                "https://static.toiimg.com/photo/54599572.cms",
                                "https://www.transindiatravels.com/wp-content/uploads/the-red-fort-delhi.jpg",
                                "https://img.traveltriangle.com/blog/wp-content/uploads/2017/01/Untitled-design-4.jpg",
                                "https://www.indianholiday.com/images/travel-destination/delhi.jpg",
                                "https://www.holidify.com/images/tooltipImages/MUNNAR.jpg",
                                "https://www.touropia.com/gfx/d/tourist-attractions-in-india/hawa_mahal.jpg?v=1",
                                "https://www.transindiatravels.com/wp-content/uploads/gateway-of-india-mumbai.jpg",
            "https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4",
            "https://cdn.cnn.com/cnnnext/dam/assets/181010131059-australia-best-beaches-cossies-beach-cocos3.jpg",
            "https://static.toiimg.com/photo/32791811.cms",
            "https://www.history.com/.image/t_share/MTU3ODc5MDg3NTA5MDg3NTYx/taj-mahal-2.jpg",
            "https://i1.wp.com/www.omanobserver.om/wp-content/uploads/2019/11/Sea-coast.jpg?fit=1500%2C840&ssl=1",
            "https://api.timeforkids.com/wp-content/uploads/2017/08/170227012793_hero.jpg",
            "https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4",
            "https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4",
            "https://cdn.cnn.com/cnnnext/dam/assets/181010131059-australia-best-beaches-cossies-beach-cocos3.jpg",
            "https://static.toiimg.com/photo/32791811.cms",
            "https://www.history.com/.image/t_share/MTU3ODc5MDg3NTA5MDg3NTYx/taj-mahal-2.jpg",
            "https://i1.wp.com/www.omanobserver.om/wp-content/uploads/2019/11/Sea-coast.jpg?fit=1500%2C840&ssl=1",
            "https://api.timeforkids.com/wp-content/uploads/2017/08/170227012793_hero.jpg",
            "https://static.toiimg.com/photo/67977105/4.jpg?width=748&resize=4")

    }
}