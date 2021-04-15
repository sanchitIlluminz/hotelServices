package com.illuminz.application.ui.cart

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.core.extensions.gone
import com.core.extensions.isNetworkActiveWithMessage
import com.core.extensions.orZero
import com.core.extensions.visible
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.cart.items.CartItem
import com.illuminz.application.ui.cart.items.TaxesDetailPopUpItem
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.FoodListFragment
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.request.CartItemDetail
import com.illuminz.data.models.request.CartRequest
import com.illuminz.data.models.response.*
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.fragment_cart.*

class LaundryCartFragment : DaggerBaseFragment(), CartBarView.Callback, CartItem.Callback {

    companion object {
        const val TAG = "CartFragment"

        private const val TAX_TYPE_FOOD = 1
        private const val TAX_TYPE_LIQUOR = 2

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_CART_ITEMS = "KEY_CART_ITEMS"

        private const val CHILD_LOADING = 0
        private const val CHILD_CONNECTION_ERROR = 1
        private const val CHILD_RESULT = 2

        fun newInstance(title: String, list: ArrayList<CartItemDetail>): LaundryCartFragment {
            val fragment = LaundryCartFragment()
            val arguments = Bundle()
            arguments.putString(KEY_TITLE, title)
            arguments.putParcelableArrayList(KEY_CART_ITEMS, list)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var cartAdapter: GroupAdapter<GroupieViewHolder>
    private var popUpAdapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()
    private lateinit var taxPopup: PopupWindow

    private var cartItemRequestList = mutableListOf<CartItemDetail>()
    private var cartItemsDetailList = mutableListOf<CartItemDto>()

    private var quantityChanged = false

    private var taxesResponse = mutableListOf<TaxesDto>()

    private lateinit var cartType: String

    private var quantityIncreasedCase = false
    private var quantityDecreasedCase = false

    private var changedCartItem: CartItem? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LaundryCartViewModel::class.java]
    }

    override fun getLayoutResId(): Int = R.layout.fragment_cart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
        setObservers()
    }

    private fun initialise() {
        cartType = requireArguments().getString(KEY_TITLE).orEmpty()
        cartItemRequestList.addAll(
            requireArguments().getParcelableArrayList(KEY_CART_ITEMS) ?: emptyList()
        )

        tvLiquorTaxes.gone()
        tvLiquorTaxesLabel.gone()
        tvFoodTaxesLabel.text = "Taxes"

        val roomNo = 111
        val groupCode = "111"
        val cartItemList = mutableListOf<CartItemDetail>()

        cartItemRequestList.forEach { cartItem ->
            cartItemList.add(cartItem)
        }

        val cartRequest = CartRequest(
            room = roomNo,
            groupCode = groupCode,
            itemList = cartItemList
        )

        cartAdapter = GroupAdapter()
        rvCart.adapter = cartAdapter

        cartBarView.gone()

        if (requireContext().isNetworkActiveWithMessage()) {
            quantityDecreasedCase = false
            quantityIncreasedCase = false

            viewModel.getLaundryCart(cartRequest)
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        tvFoodTaxesLabel.setOnClickListener {
            taxPopup = showPopUpWindow()
            taxPopup.run {
                isOutsideTouchable = true
                isFocusable = true
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                showAsDropDown(it)
            }
        }

        cartBarView.setCallback(this)
    }

    private fun setObservers() {
        viewModel.getSaveCartObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showLoading()
                }

                Status.SUCCESS -> {
                    dismissLoading()
                    showConfirmationDialog(
                        getString(R.string.order_placed),
                        getString(R.string.order_will_be_delivered_in_time)
                    )
                }

                Status.ERROR -> {
                    dismissLoading()
                    handleError(resource.error)
                }
            }
        })

        viewModel.getLaundryCartObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    viewFlipper.displayedChild = CHILD_LOADING
                }

                Status.SUCCESS -> {
                    viewFlipper.displayedChild = CHILD_RESULT
                    cartBarView.visible()
                    setBasicData(resource.data)
                }

                Status.ERROR -> {
                    viewFlipper.displayedChild = CHILD_CONNECTION_ERROR
                    handleError(resource.error)
                    updateCartList()
                }
            }
        })
    }

    private fun setBasicData(data: LaundryCartResponse?) {
        cartAdapter.clear()
        cartItemsDetailList.clear()
        taxesResponse.clear()

        data?.items?.forEach { cartItemDetails ->
            if (cartItemDetails.quantity != 0) {
                cartItemsDetailList.add(cartItemDetails)
            }
        }

        data?.taxes?.laundryTaxes?.let { taxesResponse.addAll(it) }
//        data?.taxes?.let { taxesResponse = it as MutableList<TaxesDto> }

        var itemCount = 0
        cartItemsDetailList.forEach {
            itemCount += it.quantity.orZero()
        }

        setToolbarItemCount(itemCount.orZero())

        when (cartType) {
            FoodListFragment.TAG -> {
                cartItemsDetailList.forEach { cartItemDetails ->
                    val item = CartItem(
                        itemDetails = cartItemDetails,
                        callback = this,
                        fragmentTag = FoodListFragment.TAG
                    )
                    cartAdapter.add(item)
                }
            }

            LaundryFragment.TAG -> {
                cartItemsDetailList.forEach { cartItemDetails ->
                    val item = CartItem(
                        itemDetails = cartItemDetails,
                        callback = this,
                        fragmentTag = LaundryFragment.TAG
                    )
                    cartAdapter.add(item)
                }
            }

            else -> {
                cartItemsDetailList.forEach { cartItemDetails ->
                    val item = CartItem(
                        itemDetails = cartItemDetails,
                        callback = this,
                        fragmentTag = MassageListFragment.TAG
                    )
                    cartAdapter.add(item)
                }
            }
        }

        changeCartItems()
    }

    private fun updateCartList() {
        val quantityChange = when {
            quantityIncreasedCase -> -1
            quantityDecreasedCase -> 1
            else -> 0
        }

        cartItemsDetailList.forEach { item ->
            if (item.id == changedCartItem?.itemDetails?.id) {
                item.quantity = item.quantity?.plus(quantityChange)
            }
        }
    }

    override fun onCartBarClick() {
//
//        if (requireContext().isNetworkActiveWithMessage()){
//            val roomNo = 111
//            val groupCode = "111"
//            val itemList = mutableListOf<FoodRequestDto>()
//
//            cartItemList.forEach { serviceCategoryItemDto ->
//                val foodRequestDto = FoodRequestDto(
//                    id = serviceCategoryItemDto.id,
//                    quantity = serviceCategoryItemDto.quantity
//                )
//                itemList.add(foodRequestDto)
//            }
//
//            val foodCartRequest = FoodCartRequest(
//                room = roomNo,
//                groupCode = groupCode,
//                itemList = itemList
//            )
//            viewModel.getFoodCart(foodCartRequest)
//        }
        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.saveLaundryCart(getCartRequest())
        }
    }

    override fun onIncreaseCartItemClicked(
        cartItem: CartItemDto,
        laundryItem: Boolean
    ) {
        quantityChanged = true
        quantityDecreasedCase = false
        quantityIncreasedCase = true
        if (requireContext().isNetworkActiveWithMessage()) {
            viewModel.getLaundryCart(getCartRequest())
//            viewModel.getFoodCart(getCartRequest(quantityChange = -1, changedItemId = cartItem.id))
        } else {
            cartItem.quantity = cartItem.quantity?.minus(1)
        }
        //Find and change cartItem
//        for (i in 0 until cartAdapter.groupCount) {
//            val group = cartAdapter.getGroupAtAdapterPosition(i) as CartItem
//
//            if (group.itemDetails.id == cartItem.id) {
//                if (!laundryItem        //Either not a laundry case
//                    || (laundryItem     //Or laundry case and of same type i.e only iron or wash&iron
//                            && checkSameCategoryItem(   //same category checked to differentiate between iron &wash iron item
//                        cartItem,            // since id is same for both
//                        group.itemDetails
//                    ))
//                ) {
//                    group.itemDetails.quantity = cartItem.quantity
//                    cartAdapter.notifyItemChanged(i, QuantityChangedPayload)
//                }
//                break
//            }
//        }
//        changeCartItems(serviceCategoryItem = cartItem)
    }

    override fun onDecreaseCartItemClicked(
        cartItem: CartItem,
        laundryItem: Boolean
    ) {
        quantityChanged = true
        quantityDecreasedCase = true
        quantityIncreasedCase = false
        if (requireContext().isNetworkActiveWithMessage()) {
            if (cartItem.itemDetails.quantity == 0) {
                cartItemsDetailList.forEach { item ->
                    if ((item.id == cartItem.itemDetails.id) && (item.laundryType == cartItem.itemDetails.laundryType)) {
                        item.quantity = 0
                    }
                }
            }
            viewModel.getLaundryCart(getCartRequest())
//            viewModel.getFoodCart(getCartRequest(quantityChange = -1, changedItemId = cartItem.itemDetails.id))

        } else {
            cartItem.itemDetails.quantity = cartItem.itemDetails.quantity?.plus(1)
        }
//        //Find and change cartItem
//        for (i in 0 until cartAdapter.groupCount) {
//            val group = cartAdapter.getGroupAtAdapterPosition(i) as CartItem
//
//            if (group.itemDetails.id == serviceCategoryItem.id) {
//                if (!laundryItem        //Either not a laundry case
//                    || (laundryItem     //Or laundry case and of same type i.e only iron or wash&iron
//                            && checkSameCategoryItem(   //same category checked to differentiate between iron &wash iron item
//                        serviceCategoryItem,            // since id is same for both
//                        group.itemDetails
//                    ))
//                ) {
//                    //Remove item if quantity is zero or update it
//                    if (serviceCategoryItem.quantity == 0) {
//                        cartAdapter.removeGroupAtAdapterPosition(i)
//                        cartItemList.removeAt(i)
//                    } else {
//                        group.itemDetails.quantity = serviceCategoryItem.quantity
//                        cartItemList[i].quantity = serviceCategoryItem.quantity
//                    }
//                    cartAdapter.notifyItemChanged(i, QuantityChangedPayload)
//                }
//                break
//            }
//        }
//        changeCartItems(serviceCategoryItem = serviceCategoryItem)
    }

    private fun changeCartItems(
        serviceCategoryItem: ServiceCategoryItemDto? = null
    ) {
        var cartItemCount = 0
        val totalAmount = 0.0

        cartItemsDetailList.forEach {
            cartItemCount += it.quantity.orZero()

//            // For laundry, price field is empty so check ironing and washIroning price
//            if (cartType.equals(LaundryFragment.TAG)) {
//                if (it.ironingPrice != null)
//                    totalAmount += it.ironingPrice.orZero() * it.quantity
//                else
//                    totalAmount += it.washIroningPrice.orZero() * it.quantity
//            } else {
//                totalAmount += (it.price.orZero() * it.quantity)
//            }
        }

        setToolbarItemCount(cartItemCount.orZero())

        if (cartItemsDetailList.size != 0) {
            cartBarView.visible()
            setBillDetails()
        } else {
            cartBarView.gone()
//            setBillDetails()
            requireActivity().onBackPressed()

        }
    }

    private fun setBillDetails(
    ) {
        var itemTotal = 0.0
        var totalTax = 0.0
        var cartItemCount = 0

        cartItemsDetailList.forEach { cartItemsDetail ->
            itemTotal += cartItemsDetail.cartTotal.orZero()
            cartItemCount += cartItemsDetail.quantity.orZero()
        }

        taxesResponse.forEach { tax ->
            totalTax += tax.debit.orZero()
        }

        tvItemTotal.text = CurrencyFormatter.format(
            amount = itemTotal.orZero()
        )

        tvFoodTaxes.text = CurrencyFormatter.format(
            amount = totalTax.orZero()
        )

        setTaxVisibility(totalTax, tvFoodTaxesLabel, tvFoodTaxes)

        val totalAmount = itemTotal.orZero() + totalTax.orZero()
        tvTotalAmount.text = CurrencyFormatter.format(
            amount = totalAmount.orZero()
        )

        cartBarView.setItemPrice(totalPrice = totalAmount, items = cartItemCount)
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

    private fun getCartRequest(
    ): CartRequest {
        val roomNo = 111
        val groupCode = "111"
        val cartItemDetailList = mutableListOf<CartItemDetail>()

        cartItemsDetailList.forEach { cartItem ->
            val cartItemDetail = CartItemDetail(
                id = cartItem.id,
                quantity = cartItem.quantity.orZero(),
                type = cartItem.laundryType
            )
            cartItemDetailList.add(cartItemDetail)
        }

        return CartRequest(
            room = roomNo,
            groupCode = groupCode,
            itemList = cartItemDetailList
        )
    }

    private fun showPopUpWindow(): PopupWindow {
        val contentView = View.inflate(requireContext(), R.layout.layout_tax_popup, null)
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.rvPopup)

        popUpAdapter.clear()
        recyclerView.adapter = popUpAdapter

        taxesResponse.forEach { taxes ->
            val item = TaxesDetailPopUpItem(taxes)
            popUpAdapter.add(item)
        }

        return PopupWindow(
            contentView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setTaxVisibility(totalTax: Double, taxLabel: View, taxValue: View) {
        if (totalTax == 0.0) {
            taxLabel.gone()
            taxValue.gone()
        } else {
            taxLabel.visible()
            taxValue.visible()
        }
    }
}