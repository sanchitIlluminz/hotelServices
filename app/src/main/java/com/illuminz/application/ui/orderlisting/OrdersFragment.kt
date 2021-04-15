package com.illuminz.application.ui.orderlisting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.core.extensions.cannotScrollBottom
import com.core.extensions.isNetworkActive
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.data.extensions.isConnectionError
import com.illuminz.data.models.common.Status
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "OrdersFragment"

        private const val CHILD_WALLET_ITEMS = 0
        private const val CHILD_LOADING = 1
        private const val CHILD_CONNECTION_ERROR = 2
        private const val CHILD_NO_ITEMS = 3

        fun newInstance(): OrdersFragment{
            val fragment = OrdersFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }
    }

    private val viewModel by lazy{
        ViewModelProvider(this,viewModelFactory)[OrderListingViewModel::class.java]
    }

    private lateinit var adapter: OrdersAdapter

    override fun getLayoutResId(): Int = R.layout.fragment_orders

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setUpRecycler()
        setObservers()
        viewModel.getOrdersListing()
    }


    private fun setUpRecycler() {
        adapter = OrdersAdapter()
        rvOrders.adapter = adapter
        rvOrders.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                toolbar.isSelected = recyclerView.canScrollVertically(-1)
                if (recyclerView.cannotScrollBottom() &&
                    viewModel.isValidForPaging() &&
                    requireActivity().isNetworkActive()
                ) {
                    viewModel.getOrdersListing()
                }
            }
        })
    }

    private fun initialise() {

    }

    private fun setObservers() {
        viewModel.getWalletObserver().observe(viewLifecycleOwner, Observer { resource ->
            val isFirstPage = resource.data?.isFirstPage ?: false
            when (resource.status) {
                Status.LOADING -> {
                    if (isFirstPage) {
                        viewFlipper.displayedChild = CHILD_LOADING
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
                        viewFlipper.displayedChild = CHILD_NO_ITEMS

                    } else {
                        viewFlipper.displayedChild = CHILD_WALLET_ITEMS

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
                                CHILD_CONNECTION_ERROR
                        } else {
                            viewFlipper.displayedChild = CHILD_WALLET_ITEMS
                            handleError(resource.error, view = view)
                        }
                    } else {
                        viewFlipper.displayedChild = CHILD_WALLET_ITEMS
                        handleError(resource.error, view = view)
                    }
//                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

}