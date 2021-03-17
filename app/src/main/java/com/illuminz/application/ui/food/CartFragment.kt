package com.illuminz.application.ui.food

import android.os.Bundle
import android.view.View
import com.core.extensions.gone
import com.core.extensions.orZero
import com.core.extensions.setCustomAnimations
import com.core.extensions.visible
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.items.CartItem
import com.illuminz.application.ui.food.items.FoodItem
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.application.ui.payment.PaymentMethodFragment
import com.illuminz.application.utils.QuantityChangedPayload
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.cartBarView
import kotlinx.android.synthetic.main.fragment_cart.toolbar
import kotlinx.android.synthetic.main.fragment_food_list.*

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

    override fun getLayoutResId(): Int = R.layout.fragment_cart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
//        setBasicData()
        setListeners()

    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        cartBarView.setCallback(this)
    }

    private fun setBasicData() {
        tvItemTotal.text = CurrencyFormatter.format(
            amount = 800.00, countryCode = "IN", languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2
        )
        tvServiceCharges.text = CurrencyFormatter.format(
            amount = 20.00, countryCode = "IN", languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2
        )
        tvTaxes.text = CurrencyFormatter.format(
            amount = 10.00, countryCode = "IN", languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2
        )
        tvTotalAmount.text = CurrencyFormatter.format(
            amount = 830.00, countryCode = "IN", languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2
        )
    }

    private fun initialise() {

        val title = requireArguments().getString(KEY_TITLE)
        cartItemList.addAll(
            requireArguments().getParcelableArrayList(KEY_CART_ITEMS) ?: emptyList()
        )

        toolbar.subtitle = resources.getQuantityString(
            R.plurals.cart_items,
            cartItemList.size.orZero(),
            cartItemList.size.orZero()
        )

        cartAdapter = GroupAdapter()
        rvCart.adapter = cartAdapter

        when (title) {
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

//            DrinksFragment.TAG -> listOf(CartItem(title = "Hoegarden",price = 200.00,quantity = 5,callback = this,fragmentTag = DrinksFragment.TAG),
//                CartItem(title = "Kingfisher", price = 300.00,quantity = 2,callback = this,fragmentTag = DrinksFragment.TAG),
//                CartItem(title = "Edelweiss",price = 100.00,quantity = 1,callback = this,fragmentTag = DrinksFragment.TAG),
//                CartItem(title = "Hoegarden",price = 200.00,quantity = 5,callback = this,fragmentTag = DrinksFragment.TAG),
//                CartItem(title = "oegarden Ultra Max", price = 300.00,quantity = 2,callback = this,fragmentTag = DrinksFragment.TAG)
//            )
//
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
        if (parentFragmentManager.findFragmentByTag(PaymentMethodFragment.TAG) == null) {
            val fragment = PaymentMethodFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .add(R.id.fragmentContainer, fragment)
                .addToBackStack(PaymentMethodFragment.TAG)
                .commit()
        }
    }

    override fun onIncreaseCartItemClicked(serviceCategoryItem: ServiceCategoryItemDto) {
        for (i in 0 until cartAdapter.groupCount) {
            val group = cartAdapter.getGroupAtAdapterPosition(i) as CartItem
            if (group.serviceCategoryItem.id == serviceCategoryItem.id) {
                group.serviceCategoryItem.quantity = serviceCategoryItem.quantity
                cartAdapter.notifyItemChanged(i, QuantityChangedPayload)
            }
        }
        changeCartItems(serviceCategoryItem = serviceCategoryItem)
    }

    override fun onDecreaseCartItemClicked(serviceCategoryItem: ServiceCategoryItemDto) {
        for (i in 0 until cartAdapter.groupCount) {
            val group = cartAdapter.getGroupAtAdapterPosition(i) as CartItem
            if (group.serviceCategoryItem.id == serviceCategoryItem.id) {
                if (serviceCategoryItem.quantity == 0) {
                    cartAdapter.removeGroupAtAdapterPosition(i)
                    cartItemList.removeAt(i)
                } else {
                    group.serviceCategoryItem.quantity = serviceCategoryItem.quantity
                    cartAdapter.notifyItemChanged(i, QuantityChangedPayload)
                }
            }
        }
        changeCartItems(serviceCategoryItem = serviceCategoryItem)
    }

    private fun changeCartItems(
        serviceCategoryItem: ServiceCategoryItemDto? = null
    ) {
        var itemCount = 0
        var totalPrice = 0.0

        if (serviceCategoryItem!=null){
            for (i in 0 until cartItemList.size) {
                if (cartItemList[i].id == serviceCategoryItem.id) {
                    //Remove item if quantity is zero else update quantity
                    if (serviceCategoryItem.quantity == 0) {
                        cartItemList.removeAt(i)
                    } else {
                        cartItemList[i].quantity = serviceCategoryItem.quantity
                    }
                }
            }
        }

        cartItemList.forEach {
            itemCount += it.quantity
            totalPrice += (it.price.orZero() * it.quantity)
        }

        if (cartItemList.size != 0) {
            cartBarView.visible()
            cartBarView.setItemPrice(totalPrice = totalPrice, items = itemCount)
        } else {
            cartBarView.gone()
        }

        setBillDetails(totalPrice,20.0,10.0)
    }

    private fun setBillDetails(itemTotal:Double,servicecharge:Double,taxes:Double){
        tvItemTotal.text = CurrencyFormatter.format(
            amount = itemTotal.orZero(),
            currencyCode = "INR"
        )

        tvServiceCharges.text = CurrencyFormatter.format(
            amount = servicecharge.orZero(),
            currencyCode = "INR"
        )

        tvTaxes.text = CurrencyFormatter.format(
            amount = taxes.orZero(),
            currencyCode = "INR"
        )

        val totalAmount = itemTotal + servicecharge + taxes
        tvTotalAmount.text =CurrencyFormatter.format(
            amount = totalAmount.orZero(),
            currencyCode = "INR"
        )

    }
}