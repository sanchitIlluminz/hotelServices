package com.illuminz.application.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.core.extensions.isNetworkActiveWithMessage
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.bar.DrinksFragment
import com.illuminz.application.ui.bookTable.BookTableFragment
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.home.items.*
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.application.ui.nearbyplaces.NearbyFragment
import com.illuminz.application.ui.roomcleaning.RoomCleaningFragment
import com.illuminz.application.ui.transport.TransportFragment
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_contact.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : DaggerBaseFragment() {

    companion object {
        const val TAG = "HomeFragment"
        private const val ORDER_FOOD = "food"
        private const val LAUNDRY = "laundary"
        private const val MASSAGE = "spa"
        private const val ROOM_CLEANING = "cleaning"
        private const val BAR = "BAR"
        private const val BOOK_TABLE = "tablebooking"
        private const val NEARBY = "places"
        private const val TRANSPORT = "transport"
        private const val GYM = "gym"
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

    }

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private var serviceList = mutableListOf<ServiceDto>()
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[ServicesViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListener()
        setObservers()
    }


    private fun initialise() {

        adapter = GroupAdapter()
        val layoutManager = (rvHome.layoutManager as GridLayoutManager)
        layoutManager.spanSizeLookup = adapter.spanSizeLookup
        rvHome.adapter = adapter

        if (context?.isNetworkActiveWithMessage() == true && !viewModel.serviceListAvailable()) {
            viewModel.getServices()
        } else {
            setData(viewModel.getServiceList())
        }
//        tvTimeValue.text = "15"
//        tvTimeUnit.text = "min"
//        tvDeliveryLabel.text = "It will be delivered at 4:30pm"


//        adapter.add(RoomServiceItem())

        val image1 = "https://www.howtogeek.com/wp-content/uploads/2020/03/delivery-food.jpg"
        val image2 =
            "https://cleanlaundry.com/wp-content/uploads/2016/02/9-Tips-to-Make-Ironing-Your-Clothes-a-Piece-of-Cake1-300x200.jpg"
        val image3 =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtVQMlP4qORDhewsl_ovMA2MVn9zKSgfalBg&usqp=CAU"
        val image4 =
            "https://ehlersdanlosnews.com/wp-content/uploads/2020/01/shutterstock_547127785-760x475@2x.jpg"
        val image5 =
            "https://images.unsplash.com/photo-1588710406418-bb90c807db72?ixid=MXwxMjA3fDB8MHxzZWFyY2h8Mnx8cmVzdGF1cmFudCUyMHRhYmxlfGVufDB8fDB8&ixlib=rb-1.2.1&w=1000&q=80"
        val image6 =
            "https://www.designmantic.com/blog/wp-content/uploads/2020/04/Spa-and-Massage-Logos.jpg"

        val image7 =
            "https://cdn.britannica.com/95/94195-050-FCBF777E/Golden-Gate-Bridge-San-Francisco.jpg"

        val image8 =
            "https://www.thoughtco.com/thmb/2-kR4cU4EOrY8bQ1M8msDinZgG8=/1926x1280/filters:fill(auto,1)/GettyImages-556712867-58e85b223df78c51620400d4.jpg"

        val image9 =
            "https://s9i4v6w2.rocketcdn.me/wordpress/wp-content/uploads/2020/10/Pullman-Accor-Power-Fitness-Gym-Design-1-400x284.jpg"


    }


    private fun setListener() {

        btnFeedback.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val fragment = FeedbackFragment.newInstance()
            openFragment(fragment, FeedbackFragment.TAG)
        }

        btnWifi.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val list = getWifiList()
            showContactDialog(list, 2)
        }

        btnContact.setOnClickListener {
            val list = getContactList()
            showContactDialog(list, 1)
        }

        adapter.setOnItemClickListener { item, _ ->

            if (item is ServicesItem) {
                when (item.serviceDto.tag) {
                    ORDER_FOOD -> {
                        val fragment = FoodListFragment.newInstance(
                            id = item.serviceDto.id.orEmpty(),
                            tag = item.serviceDto.tag.orEmpty()
                        )
                        openFragment(fragment, FoodListFragment.TAG)
                    }

                    MASSAGE -> {
                        val fragment = MassageListFragment.newInstance()
                        openFragment(fragment, MassageListFragment.TAG)
                    }

                    ROOM_CLEANING -> {
                        val fragment = RoomCleaningFragment.newInstance()
                        openFragment(fragment, RoomCleaningFragment.TAG)
                    }

                    BAR -> {
                        val fragment = DrinksFragment.newInstance()
                        openFragment(fragment, DrinksFragment.TAG)
                    }
                    BOOK_TABLE -> {
                        val fragment = BookTableFragment.newInstance()
                        openFragment(fragment, BookTableFragment.TAG)
                    }
                    NEARBY -> {
                        val fragment = NearbyFragment.newInstance(
                            getString(R.string.nearby_places),
                            getString(R.string.places_to_visit), nearbyList()
                        )
                        openFragment(fragment, NearbyFragment.TAG)
                    }

                }
            } else if (item is MoreServicesItem) {
                when (item.serviceDto.tag) {


                    TRANSPORT -> {
//                            val url = "https://images.newindianexpress.com/uploads/user/imagelibrary/2019/10/20/w900X450/North_DMC.jpg"
//                            val title = "New Delhi Railway Station"
//                            val price = CurrencyFormatter.format(amount = 1000.00, currencyCode = "INR")
//                            val distance = "220km"

                        val fragment = TransportFragment.newInstance()
                        openFragment(fragment, TransportFragment.TAG)
                    }

                    GYM -> {
                        val fragment = NearbyFragment.newInstance(
                            getString(R.string.gym_features),
                            getString(R.string.gym_subtitle), gymList()
                        )
                        openFragment(fragment, NearbyFragment.TAG)
                    }

                    LAUNDRY -> {
                        val fragment = LaundryFragment.newInstance(
                            serviceId = item.serviceDto.id.orEmpty(),
                            serviceTag = item.serviceDto.tag.orEmpty()
                        )
                        openFragment(fragment, LaundryFragment.TAG)
                    }
                }
            }

        }
    }


    private fun setObservers() {
        viewModel.getServiceObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showLoading()
                }
                Status.SUCCESS -> {
                    dismissLoading()
                    resource.data?.let {
                        serviceList.addAll(it)
                        setData(it)
                    }
                }
                Status.ERROR -> {
                    dismissLoading()
                    handleError(resource.error)
                }
            }
        })
    }

    private fun setData(list: List<ServiceDto>) {
        adapter.clear()
        adapter.add(BookingDetailItem())
        adapter.add(ServiceTitleItem(getString(R.string.services)))

        for (i in list.indices) {
            if (i < 6) {
                val item = ServicesItem(list[i])
                adapter.add(item)
            } else {
                if (i == 6) {
                    adapter.add(ServiceTitleItem("MORE SERVICES"))
                }
                val item = MoreServicesItem(list[i])
                adapter.add(item)
            }
        }
    }

    private fun openFragment(fragment: DaggerBaseFragment, tag: String) {
        if (parentFragmentManager.findFragmentByTag(tag) == null) {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

    private fun nearbyList(): ArrayList<String> {
        val imageList = arrayListOf(
            "https://2.bp.blogspot.com/-kIlA_BjfIqQ/T-CQ8WuaC7I/AAAAAAAAAkE/dTo_8UZxBBU/s1600/Mumbai+322.JPG",
            "https://www.visittnt.com/blog/wp-content/uploads/2018/05/Marine-drive.jpg",
            "https://lp-cms-production.imgix.net/2019-06/99ad019a84817e4bb5248d47c70dd1d2-chhatrapati-shivaji-terminus.jpg",
            "https://static.toiimg.com/photo/61984337.cms",
            "https://static.toiimg.com/photo/54599572.cms",
            "https://www.fabhotels.com/blog/wp-content/uploads/2019/06/Haji-Ali-Dargah_600-1280x720.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/6/62/Mumbai_Dhobi_Ghat_Laundry_District.JPG",
            "https://www.indianholiday.com/images/travel-destination/delhi.jpg",
            "https://www.holidify.com/images/tooltipImages/MUNNAR.jpg"
        )

        return imageList
    }

    private fun gymList(): ArrayList<String> {
        val imageList = arrayListOf(
            "https://img.webmd.com/dtmcms/live/webmd/consumer_assets/site_images/article_thumbnails/blog_posts/webmd-doctors/1800x1200_empty-gym.jpg",
            "https://www.telegraph.co.uk/content/dam/luxury/2017/03/21/Gym_2_trans_NvBQzQNjv4BqgsaO8O78rhmZrDxTlQBjdGcv5yZLmao6LolmWYJrXns.jpg",
            "https://blog.nasm.org/hubfs/cleangym%20%281%29.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm1YU1rJ6caLIsbCAvfqLCcarmMyvFZISZZw&usqp=CAU",
            "https://www.gannett-cdn.com/presto/2020/04/21/USAT/6d4af31f-c7f7-4ade-b570-515252b9c81e-XXX_fitness_center.jpg"
        )

        return imageList
    }

    private fun showContactDialog(menuList: List<ContactDialogItem>, type: Int) {

        val dialog = context?.let { Dialog(it) }

        val contactAdapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_contact)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
//                attributes.x = context.dpToPx(24) // left margin
//                attributes.y = context.dpToPx(140) // bottom margin
            }

            if (type == 1) {
                tvTitle.text = getString(R.string.contact_detail)
                tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_contact,
                    0,
                    0,
                    0
                )
            } else {
                tvTitle.text = getString(R.string.wifi)
                tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_wifi, 0, 0, 0)
            }

            rvDialogContact.adapter = contactAdapter
//
            contactAdapter.addAll(menuList)

            contactAdapter.setOnItemClickListener { item, view ->
                dismiss()
            }

            btnOkay.setOnClickListener {
                dismiss()
            }
        }

        dialog?.show()
    }

    private fun getContactList(): List<ContactDialogItem> {
        return listOf(
            ContactDialogItem(title = "Reception", value = "119"),
            ContactDialogItem(title = "Hotel", value = "220"),
            ContactDialogItem(title = "Emergency", value = "330")
        )
    }

    private fun getWifiList(): List<ContactDialogItem> {
        return listOf(
            ContactDialogItem(title = "Name", value = "EmpireWifi1"),
            ContactDialogItem(title = "Paasword", value = "ANM230v1")
        )
    }

}