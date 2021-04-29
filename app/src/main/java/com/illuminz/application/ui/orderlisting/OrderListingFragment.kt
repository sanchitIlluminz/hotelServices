package com.illuminz.application.ui.orderlisting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.core.extensions.cannotScrollBottom
import com.core.extensions.isNetworkActive
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.orderlisting.items.OrderItem
import com.illuminz.application.ui.orderlisting.items.RequestOrderItem
import com.illuminz.data.extensions.isConnectionError
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.SaveFoodOrderResponse
import com.illuminz.data.models.response.ServiceRequestDto
import com.illuminz.data.models.response.ServiceRequestResponse
import kotlinx.android.synthetic.main.fragment_order_listing.*

class OrderListingFragment : DaggerBaseFragment(), OrderItem.Callback {
    companion object {
        const val TAG = "OrdersListingFragment"

        private const val FLIPPER_RESULT_ITEMS = 0
        private const val FLIPPER_LOADING = 1
        private const val FLIPPER_CONNECTION_ERROR = 2
        private const val FLIPPER_NO_ITEMS = 3

        private const val ORDER_TYPE = "ORDER_TYPE"

        fun newInstance(orderType: Int): OrderListingFragment {
            val fragment = OrderListingFragment()
            val arguments = Bundle()
            arguments.putInt(ORDER_TYPE, orderType)
            fragment.arguments = arguments
            return fragment
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OrderListingViewModel::class.java]
    }

    private lateinit var adapter: OrdersAdapter

    private var orderType = -1

    override fun getLayoutResId(): Int = R.layout.fragment_order_listing

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setUpRecycler()
        setObservers()
        setListeners()
        if (orderType != AppConstants.ORDER_TYPE_OTHERS) {
            viewModel.getOrdersListing(orderType = orderType)
        } else{
            val roomDetailHandler = viewModel.getRoomHandler()
            val roomNo = roomDetailHandler.roomDetails.roomNo
            val groupCode = roomDetailHandler.roomDetails.groupCode
            viewModel.getServiceRequest(roomNo, groupCode, AppConstants.SERVICE_REQUEST_TYPE_ALL)
        }
//
    }

    private fun setListeners() {
        adapter.setOnItemClickListener { item, view ->
//            if (item is OrderItem && item.orderResponse != null) {
//                val parent = parentFragment
//                if (parent is OrderClickListener) {
//                    item.orderResponse?.orderDetail?.let { parent.openOrderDetail(it) }
//                }
//                if (parentFragmentManager.findFragmentByTag(OrderDetailFragment.TAG) == null) {
//                    parentFragmentManager.beginTransaction()
//                        .setCustomAnimations(AnimationDirection.End)
//                        .replace(
//                            R.id.fragmentContainer,
//                            OrderDetailFragment.newInstance(item.orderResponse),
//                            OrderDetailFragment.TAG
//                        )
//                        .addToBackStack(tag)
//                        .commit()
//                }
//            }
        }
    }


    private fun setUpRecycler() {
        rvOrders.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.cannotScrollBottom() &&
                    viewModel.isValidForPaging() &&
                    requireActivity().isNetworkActive() && (orderType!=AppConstants.ORDER_TYPE_OTHERS)
                ) {
                    viewModel.getOrdersListing(orderType = orderType)
                }
            }
        })
    }

    private fun initialise() {
        orderType = requireArguments().getInt(ORDER_TYPE)
        adapter = OrdersAdapter(this)
        rvOrders.adapter = adapter
    }

    private fun setObservers() {
        viewModel.getOrdersObserver().observe(viewLifecycleOwner, Observer { resource ->
            val isFirstPage = resource.data?.isFirstPage ?: false
            when (resource.status) {
                Status.LOADING -> {
                    if (isFirstPage) {
                        viewFlipper.displayedChild = FLIPPER_LOADING
                    } else {
                        adapter.setLoading(true)
                        rvOrders.scrollToPosition(adapter.getLoadingPosition())
                    }
                }
                Status.SUCCESS -> {
                    val items = resource.data?.result?.data as MutableList
                    adapter.addItems(
                        isFirstPage,
                        items
                    )
                    if (adapter.itemCount == 0) {
                        viewFlipper.displayedChild = FLIPPER_NO_ITEMS

                    } else {
                        viewFlipper.displayedChild = FLIPPER_RESULT_ITEMS

                        if (isFirstPage) {
                            rvOrders.scrollToPosition(0)
                        }
                    }
//                    swipeRefreshLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    if (!isFirstPage) {
                        adapter.setLoading(false)
                    }
                    if (resource.isConnectionError()) {
                        if (isFirstPage) {
                            viewFlipper.displayedChild =
                                FLIPPER_CONNECTION_ERROR
                        } else {
                            viewFlipper.displayedChild = FLIPPER_RESULT_ITEMS
                            handleError(resource.error, view = view)
                        }
                    } else {
                        viewFlipper.displayedChild = FLIPPER_RESULT_ITEMS
                        handleError(resource.error, view = view)
                    }
//                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })

        viewModel.getServiceResponseObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = FLIPPER_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = FLIPPER_RESULT_ITEMS
                    resource.data?.let { setRequestData(it) }
                }

                Status.ERROR -> {
                    FLIPPER_CONNECTION_ERROR
                }
            }
        })
    }

    private fun setRequestData(serviceRequestResponse: ServiceRequestResponse) {
        val list = mutableListOf<ServiceRequestDto>()
        serviceRequestResponse.massageList?.let { list.addAll(it) }
        serviceRequestResponse.serviceList?.let { list.addAll(it) }

        list.forEach {
            val item = RequestOrderItem(it)
            adapter.add(item)
        }
    }

    override fun onViewDetailsClicked(orderResponse: SaveFoodOrderResponse?) {
        val parent = parentFragment
        if (parent is OrderClickListener && orderResponse != null) {
            orderResponse.orderDetail?.let { parent.openOrderDetail(it,orderType) }
        }
    }

}