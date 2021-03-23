package com.illuminz.application.ui.cart

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.core.extensions.gone
import com.core.extensions.orZero
import com.core.extensions.visible
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.food.items.CartItem
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.cartBarView
import kotlinx.android.synthetic.main.fragment_cart.toolbar

class CartFragment : DaggerBaseFragment(), CartBarView.Callback, CartItem.Callback {

    companion object {
        const val TAG = "CartFragment"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_CART_ITEMS = "KEY_CART_ITEMS"

        fun newInstance(title: String, list: ArrayList<ServiceCategoryItemDto>): CartFragment {
            val fragment = CartFragment()
            val arguments = Bundle()
            arguments.putString(KEY_TITLE, title)
            arguments.putParcelableArrayList(KEY_CART_ITEMS, list)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var cartAdapter: GroupAdapter<GroupieViewHolder>

    private var cartItemList = mutableListOf<ServiceCategoryItemDto>()

    private lateinit var cartType: String

    override fun getLayoutResId(): Int = R.layout.fragment_cart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        cartBarView.setCallback(this)
    }

    private fun initialise() {
        cartType = requireArguments().getString(KEY_TITLE).orEmpty()
        cartItemList.addAll(
            requireArguments().getParcelableArrayList(KEY_CART_ITEMS) ?: emptyList()
        )

        var itemCount = 0
        cartItemList.forEach {
            itemCount += it.quantity
        }

        setToolbarItemCount(itemCount.orZero())

        cartAdapter = GroupAdapter()
        rvCart.adapter = cartAdapter

        when (cartType) {
            FoodListFragment.TAG -> {
                cartItemList.forEach { categoryItem ->
                    val item = CartItem(
                        serviceCategoryItem = categoryItem,
                        callback = this,
                        fragmentTag = FoodListFragment.TAG
                    )
                    cartAdapter.add(item)
                }
            }

            LaundryFragment.TAG -> {
                cartItemList.forEach { categoryItem ->
                    val item = CartItem(
                        serviceCategoryItem = categoryItem,
                        callback = this,
                        fragmentTag = LaundryFragment.TAG
                    )
                    cartAdapter.add(item)
                }
            }

            else -> {
                cartItemList.forEach { categoryItem ->
                    val item = CartItem(
                        serviceCategoryItem = categoryItem,
                        callback = this,
                        fragmentTag = MassageListFragment.TAG
                    )
                    cartAdapter.add(item)
                }
            }
        }

        changeCartItems()
        cartBarView.setButtonText(getString(R.string.proceed_to_pay))
    }

    override fun onCartBarClick() {
        showConfirmationDialog(getString(R.string.order_placed), getString(R.string.order_will_be_delivered_in_time))
    }

    override fun onIncreaseCartItemClicked(
        serviceCategoryItem: ServiceCategoryItemDto,
        laundryItem: Boolean
    ) {
        //Find and change cartItem
        for (i in 0 until cartAdapter.groupCount) {
            val group = cartAdapter.getGroupAtAdapterPosition(i) as CartItem

            if (group.serviceCategoryItem.id == serviceCategoryItem.id) {
                if (!laundryItem        //Either not a laundry case
                    || (laundryItem     //Or laundry case and of same type i.e only iron or wash&iron
                            && checkSameCategoryItem(   //same category checked to differentiate between iron &wash iron item
                        serviceCategoryItem,            // since id is same for both
                        group.serviceCategoryItem
                    ))
                ) {
                    group.serviceCategoryItem.quantity = serviceCategoryItem.quantity
                    cartAdapter.notifyItemChanged(i, QuantityChangedPayload)
                }
                break
            }
        }
        changeCartItems(serviceCategoryItem = serviceCategoryItem)
    }

    override fun onDecreaseCartItemClicked(
        serviceCategoryItem: ServiceCategoryItemDto,
        laundryItem: Boolean
    ) {
        //Find and change cartItem
        for (i in 0 until cartAdapter.groupCount) {
            val group = cartAdapter.getGroupAtAdapterPosition(i) as CartItem

            if (group.serviceCategoryItem.id == serviceCategoryItem.id) {
                if (!laundryItem        //Either not a laundry case
                    || (laundryItem     //Or laundry case and of same type i.e only iron or wash&iron
                            && checkSameCategoryItem(   //same category checked to differentiate between iron &wash iron item
                        serviceCategoryItem,            // since id is same for both
                        group.serviceCategoryItem
                    ))
                ) {
                    //Remove item if quantity is zero or update it
                    if (serviceCategoryItem.quantity == 0) {
                        cartAdapter.removeGroupAtAdapterPosition(i)
                        cartItemList.removeAt(i)
                    } else {
                        group.serviceCategoryItem.quantity = serviceCategoryItem.quantity
                        cartItemList[i].quantity = serviceCategoryItem.quantity
                    }
                    cartAdapter.notifyItemChanged(i, QuantityChangedPayload)
                }
                break
            }
        }
        changeCartItems(serviceCategoryItem = serviceCategoryItem)
    }

    private fun changeCartItems(
        serviceCategoryItem: ServiceCategoryItemDto? = null
    ) {
        var cartItemCount = 0
        var totalAmount = 0.0

        cartItemList.forEach {
            cartItemCount += it.quantity

            // For laundry, price field is empty so check ironing and washIroning price
            if (cartType.equals(LaundryFragment.TAG)) {
                if (it.ironingPrice != null)
                    totalAmount += it.ironingPrice.orZero() * it.quantity
                else
                    totalAmount += it.washIroningPrice.orZero() * it.quantity
            } else {
                totalAmount += (it.price.orZero() * it.quantity)
            }
        }

        setToolbarItemCount(cartItemCount.orZero())

        if (cartItemList.size != 0) {
            cartBarView.visible()
            cartBarView.setItemPrice(totalPrice = totalAmount, items = cartItemCount)
            setBillDetails(totalAmount, 20.0, 10.0)
        } else {
            cartBarView.gone()
            setBillDetails()
        }
    }

    private fun setBillDetails(
        totalItemPrice: Double? = null,
        serviceCharge: Double? = null,
        taxes: Double? = null
    ) {
        tvItemTotal.text = CurrencyFormatter.format(
            amount = totalItemPrice.orZero(),
            currencyCode = "INR"
        )

        tvServiceCharges.text = CurrencyFormatter.format(
            amount = serviceCharge.orZero(),
            currencyCode = "INR"
        )

        tvTaxes.text = CurrencyFormatter.format(
            amount = taxes.orZero(),
            currencyCode = "INR"
        )

        val totalAmount = totalItemPrice.orZero() + serviceCharge.orZero() + taxes.orZero()
        tvTotalAmount.text = CurrencyFormatter.format(
            amount = totalAmount.orZero(),
            currencyCode = "INR"
        )
    }

    private fun checkSameCategoryItem(
        serviceCategoryItem1: ServiceCategoryItemDto,
        serviceCategoryItem2: ServiceCategoryItemDto
    ): Boolean {
        return if (serviceCategoryItem1.ironingPrice == serviceCategoryItem2.ironingPrice) {
            true
        } else serviceCategoryItem1.washIroningPrice == serviceCategoryItem2.washIroningPrice
    }

    private fun setToolbarItemCount(count: Int) {
        toolbar.subtitle = resources.getQuantityString(
            R.plurals.cart_items,
            count,
            count
        )
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
}