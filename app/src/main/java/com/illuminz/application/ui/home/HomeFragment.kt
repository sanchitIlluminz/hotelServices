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
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.bar.DrinksFragment
import com.illuminz.application.ui.bookTable.BookTableFragment
import com.illuminz.application.ui.custom.ErrorView
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.home.items.*
import com.illuminz.application.ui.housekeeping.HouseKeepingFragment
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.application.ui.nearbyplaces.NearbyFragment
import com.illuminz.application.ui.orderlisting.OrderListingFragment
import com.illuminz.application.ui.orderlisting.OrdersFragment
import com.illuminz.application.ui.pickUpLuggage.PickLuggageFragment
import com.illuminz.application.ui.roomcleaning.RoomCleaningFragment
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.BuffetDto
import com.illuminz.data.models.response.GuestInfoResponse
import com.illuminz.data.models.response.ServiceDto
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.dialog_contact.*
import kotlinx.android.synthetic.main.dialog_contact.btnOkay
import kotlinx.android.synthetic.main.dialog_contact.tvTitle
import kotlinx.android.synthetic.main.dialog_drink.btConfirm
import kotlinx.android.synthetic.main.dialog_extend_stay.*
import kotlinx.android.synthetic.main.dialog_transport.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : DaggerBaseFragment(), BookingDetailItem.Callback, ErrorView.ErrorButtonClickListener  {
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
        private const val LUGGAGE = "LUGGAGE"
        private const val ORDERS = "ORDERS"

        private const val FLIPPER_CHILD_RESULT = 0
        private const val FLIPPER_CHILD_CONNECTION_ERROR = 2
        private const val FLIPPER_CHILD_LOADING = 1

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    private lateinit var guestInfoResponse: GuestInfoResponse

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

        val a =  viewModel.getServiceObserver().value?.isSuccess()
        if (requireContext().isNetworkActiveWithMessage() &&
            viewModel.getServiceObserver().value?.isSuccess() != true) {
            viewModel.getServices(111, "1618486040534")
        }else{
            viewModel.getServiceObserver().value?.data?.first?.let { setData(it) }
        }
    }

    private fun setListener() {
        btnFeedback.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val fragment = FeedbackFragment.newInstance(111,"1618486040534")
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

        connectionErrorView.setErrorButtonClickListener(this)

        adapter.setOnItemClickListener { item, _ ->

            if (item is ServicesItem) {
                when (item.serviceDto.tag) {
                    ORDER_FOOD -> {
                        val buffetList = guestInfoResponse.buffet
                        val fragment = FoodListFragment.newInstance(
                            serviceId = item.serviceDto.id.orEmpty(),
                            serviceTag = item.serviceDto.tag.orEmpty(),
                            buffet = buffetList as ArrayList<BuffetDto>
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
                            serviceTag = item.serviceDto.tag.orEmpty(),
                            fragmentType = AppConstants.FRAGMENT_TYPE_NEARBY
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
                        val fragment = NearbyFragment.newInstance(
                            serviceId = item.serviceDto.id.orEmpty(),
                            serviceTag = item.serviceDto.tag.orEmpty(),
                            fragmentType = AppConstants.FRAGMENT_TYPE_GYM
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

                    LUGGAGE -> {
                        val fragment = PickLuggageFragment.newInstance()
                        openFragment(fragment, PickLuggageFragment.TAG)
                    }

                    ORDERS -> {
                        val fragment = OrdersFragment.newInstance()
                        openFragment(fragment, OrdersFragment.TAG)
                    }
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.getServiceObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                   viewFlipper.displayedChild = FLIPPER_CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_RESULT
                    resource.data?.let { pairResponse ->    //Pair<List<ServiceDto>, GuestInfoResponse>
                        pairResponse.second?.let { guestInfoResponse = it }
                        serviceList.addAll(pairResponse.first)
                        setData(pairResponse.first)
                    }
                }

                Status.ERROR -> {
                    viewFlipper.displayedChild = FLIPPER_CHILD_CONNECTION_ERROR
                    handleError(resource.error)
                }
            }
        })
    }

    private fun setData(list: List<ServiceDto>) {
        adapter.clear()
        guestInfoResponse.userInfo?.let {  adapter.add(BookingDetailItem(this,it)) }
        adapter.add(ServiceTitleItem(getString(R.string.services)))

        val luggageItem = ServiceDto(title = "Pick Luggage", tag = "LUGGAGE")
        val ordersItem = ServiceDto(title = "ORDERS", tag = "ORDERS")

        list.forEachIndexed { index, service ->
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

        adapter.add(MoreServicesItem(luggageItem))
        adapter.add(MoreServicesItem(ordersItem))
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
                    "Thank you",""
                )
            }
            ivClose.setOnClickListener {
                dismiss()
            }
        }

        dialog?.show()
    }

    private fun getContactList(): List<ContactDialogItem> {
        val receptionNumber = guestInfoResponse.settings?.reception_contact_number
        val hotelNumber = guestInfoResponse.settings?.hotel_contact_number
        val emergencyNumber = guestInfoResponse.settings?.emergency_contact_number
        return listOf(
            ContactDialogItem(title = "Reception", value = receptionNumber.orEmpty()),
            ContactDialogItem(title = "Hotel", value = hotelNumber.orEmpty()),
            ContactDialogItem(title = "Emergency", value = emergencyNumber.orEmpty())
        )
    }

    private fun getWifiList(): List<ContactDialogItem> {
        val name = guestInfoResponse.settings?.wifi_name
        val password = guestInfoResponse.settings?.wifi_password
        return listOf(
            ContactDialogItem(title = "Name", value = name.orEmpty()),
            ContactDialogItem(title = "Password", value = password.orEmpty())
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
                showConfirmationDialog(
                    "Thank you",
                    "we will call you in next 5 mins")
            }
            show()
        }
    }

    override fun onErrorButtonClicked() {
        if (requireContext().isNetworkActiveWithMessage() &&
            viewModel.getServiceObserver().value?.isSuccess() != true) {
            viewModel.getServices(111, "1618486040534")
        }
    }
}