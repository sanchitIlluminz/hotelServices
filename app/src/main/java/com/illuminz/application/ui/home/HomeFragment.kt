package com.illuminz.application.ui.home

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
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.bar.DrinksFragment
import com.illuminz.application.ui.bookTable.BookTableFragment
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.home.items.*
import com.illuminz.application.ui.housekeeping.HouseKeepingFragment
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.application.ui.nearbyplaces.NearbyFragment
import com.illuminz.application.ui.roomcleaning.RoomCleaningFragment
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.dialog_contact.*
import kotlinx.android.synthetic.main.dialog_contact.btnOkay
import kotlinx.android.synthetic.main.dialog_contact.tvTitle
import kotlinx.android.synthetic.main.dialog_drink.*
import kotlinx.android.synthetic.main.dialog_drink.btConfirm
import kotlinx.android.synthetic.main.dialog_extend_stay.*
import kotlinx.android.synthetic.main.dialog_transport.*
import kotlinx.android.synthetic.main.fragment_home.*
class HomeFragment : DaggerBaseFragment(), BookingDetailItem.Callback {
    companion object {
        const val TAG = "HomeFragment"

        private const val ORDER_FOOD = "food"
        private const val LAUNDRY = "laundary"
        private const val MASSAGE = "spa"
        private const val HOUSE_KEEPING = "housekeeping"
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

        if (requireContext().isNetworkActiveWithMessage() && !viewModel.serviceListAvailable()) {
            viewModel.getServices()
        }
        if (viewModel.serviceListAvailable()) {
            setData(viewModel.getServiceList())
        }
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
                            serviceId = item.serviceDto.id.orEmpty(),
                            serviceTag = item.serviceDto.tag.orEmpty()
                        )
                        openFragment(fragment, FoodListFragment.TAG)
                    }

                    MASSAGE -> {
                        val fragment = MassageListFragment.newInstance(
                            serviceId = item.serviceDto.id.orEmpty(),
                            serviceTag = item.serviceDto.tag.orEmpty()
                        )
                        openFragment(fragment, MassageListFragment.TAG)
                    }

                    HOUSE_KEEPING -> {
                        val fragment = HouseKeepingFragment.newInstance(
                            serviceId = item.serviceDto.id.orEmpty(),
                            serviceTag = item.serviceDto.tag.orEmpty()
                        )
                        openFragment(fragment, HouseKeepingFragment.TAG)
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
                            serviceId = item.serviceDto.id.orEmpty(),
                            serviceTag = item.serviceDto.tag.orEmpty()
                        )
                        openFragment(fragment, NearbyFragment.TAG)
                    }

                }
            } else if (item is MoreServicesItem) {
                when (item.serviceDto.tag) {
                    TRANSPORT -> {
//                        val fragment = TransportFragment.newInstance()
//                        openFragment(fragment, TransportFragment.TAG)
                        showTransportDialog()
                    }

                    GYM -> {
//                        val fragment = NearbyFragment.newInstance(
//                            getString(R.string.gym_features),
//                            getString(R.string.gym_subtitle), gymList()
//                        )
//                        openFragment(fragment, NearbyFragment.TAG)
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
        adapter.add(BookingDetailItem(this))
        adapter.add(ServiceTitleItem(getString(R.string.services)))

        list.forEachIndexed{index, service ->
            val item = if (index < 6) {
               ServicesItem(service)
            } else {
                if (index == 6) {
                    adapter.add(ServiceTitleItem("MORE SERVICES"))
                }
                 MoreServicesItem(service)
            }
            adapter.add(item)
        }

//        for (i in list.indices) {
//            if (i < 6) {
//                val item = ServicesItem(list[i])
//                adapter.add(item)
//            } else {
//                if (i == 6) {
//                    adapter.add(ServiceTitleItem("MORE SERVICES"))
//                }
//                val item = MoreServicesItem(list[i])
//                adapter.add(item)
//            }
//        }
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

    private fun showExtendStayDialog() {
        val dialog = context?.let { Dialog(it) }

        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_extend_stay)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
            }

            btConfirm.setOnClickListener {
                dismiss()
                showConfirmationDialog(
                    "Thank you", "Our team will contact you \n" +
                            "within 10 mins."
                )
            }
            ivClose.setOnClickListener {
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
            ContactDialogItem(title = "Password", value = "ANM230v1")
        )
    }

    override fun onExtendStayClicked() {
        showExtendStayDialog()
    }

    private fun showConfirmationDialog(title: String, subtitle: String) {
        val dialog = context?.let { Dialog(it) }

        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_confirm)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
            }

            tvTitle.text = title
            tvSubtitle.text = subtitle

            btnOkay.setOnClickListener {
                dismiss()
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            show()
        }
    }

    private fun showTransportDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_transport)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.CENTER)
            }

            btRequest.setOnClickListener {
                dismiss()
            }
            show()
        }
    }
}