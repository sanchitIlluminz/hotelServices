package com.illuminz.application.ui.food

import android.os.Bundle
import android.view.View
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.bar.DrinksFragment
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.items.CartItem
import com.illuminz.application.ui.laundry.LaundryFragment
import com.illuminz.application.ui.massage.MassageListFragment
import com.illuminz.application.ui.payment.PaymentMethodFragment
import com.illuminz.data.utils.CurrencyFormatter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : DaggerBaseFragment(), CartBarView.Callback, CartItem.Callback {
    override fun getLayoutResId(): Int = R.layout.fragment_cart

    companion object {
       const val TAG ="CartFragment"
        private const val KEY_TITLE_1= "KEY_TITLE_1"
        fun newInstance(title:String):CartFragment{
            val fragment = CartFragment()
            val args = Bundle()
            args.putString(KEY_TITLE_1,title)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var cartItemAdapter : GroupAdapter<GroupieViewHolder>
    private lateinit var item: List<CartItem>
    private var totalPrice:Double = 0.00

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setBasicData()
        setListeners()

        val c = arguments?.getString(KEY_TITLE_1)
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

//        tvCoupon.setOnClickListener {
//
//        }

        cartBarView.setCallback(this)
    }

    private fun setBasicData() {
        tvItemTotal.text = CurrencyFormatter.format(amount = 800.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)
        tvServiceCharges.text = CurrencyFormatter.format(amount = 20.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)
        tvTaxes.text = CurrencyFormatter.format(amount = 10.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)
        tvTotalAmount.text = CurrencyFormatter.format(amount = 830.00,countryCode = "IN",languageCode = "en_in",
            currencyCode = "INR", fractionDigits = 2)
    }

    private fun initialise() {
        cartBarView.setButtonText(getString(R.string.proceed_to_pay))
        cartBarView.setItemPrice(totalPrice = 820.00,items = 4)

        toolbar.subtitle = resources.getQuantityString(R.plurals.cart_items, 6, 6)
        cartItemAdapter = GroupAdapter()
        rvCart.adapter = cartItemAdapter

        val title = arguments?.getString(KEY_TITLE_1)

        val  item1 = when(title){
            FoodListFragment.TAG -> listOf(CartItem(title = "Chocolate and Strawberry Pancake",price = 200.00,quantity = 5,callback = this,fragmentTag = FoodListFragment.TAG),
                CartItem(title = "Stuffed Pranthas with Butter - 4 Pc", price = 300.00,quantity = 2,callback = this,fragmentTag = FoodListFragment.TAG),
                CartItem(title = "Vegetable Salad with Mint Mayo",price = 100.00,quantity = 1,callback = this,fragmentTag = FoodListFragment.TAG),
                CartItem(title = "Chocolate and Strawberry Pancake",price = 200.00,quantity = 5,callback = this,fragmentTag = FoodListFragment.TAG),
                CartItem(title = "Stuffed Pranthas with Butter - 4 Pc", price = 300.00,quantity = 2,callback = this,fragmentTag = FoodListFragment.TAG),
                CartItem(title = "Vegetable Salad with Mint Mayo",price = 100.00,callback = this,fragmentTag = FoodListFragment.TAG)
            )

            LaundryFragment.TAG -> listOf(CartItem(title = "Shirt",price = 200.00,quantity = 5,callback = this,fragmentTag = LaundryFragment.TAG),
                CartItem(title = "T Shirt", price = 300.00,quantity = 2,callback = this,fragmentTag = LaundryFragment.TAG),
                CartItem(title = "Jeans",price = 100.00,quantity = 1,callback = this,fragmentTag = LaundryFragment.TAG),
                CartItem(title = "Trousers",price = 200.00,quantity = 5,callback = this,fragmentTag = LaundryFragment.TAG),
                CartItem(title = "Blazer", price = 300.00,quantity = 2,callback = this,fragmentTag = LaundryFragment.TAG)
            )

            DrinksFragment.TAG -> listOf(CartItem(title = "Hoegarden",price = 200.00,quantity = 5,callback = this,fragmentTag = DrinksFragment.TAG),
                CartItem(title = "Kingfisher", price = 300.00,quantity = 2,callback = this,fragmentTag = DrinksFragment.TAG),
                CartItem(title = "Edelweiss",price = 100.00,quantity = 1,callback = this,fragmentTag = DrinksFragment.TAG),
                CartItem(title = "Hoegarden",price = 200.00,quantity = 5,callback = this,fragmentTag = DrinksFragment.TAG),
                CartItem(title = "oegarden Ultra Max", price = 300.00,quantity = 2,callback = this,fragmentTag = DrinksFragment.TAG)
            )

            else-> listOf(CartItem(title = "Choice of Full Body Massage (50 mins) + Shower (10 mins)",price = 200.00,quantity = 5,callback = this,fragmentTag = MassageListFragment.TAG),
                CartItem(title = "Ayurvedic Shirodhara Treatment (60 mins)", price = 300.00,quantity = 2,callback = this,fragmentTag = MassageListFragment.TAG),
                CartItem(title = "Aroma Full Body Massage (45 mins) + Shower (15 mins)",price = 100.00,quantity = 1,callback = this,fragmentTag = MassageListFragment.TAG),
                CartItem(title = "Ayurvedic Shirodhara Treatment (60 mins)",price = 200.00,quantity = 5,callback = this,fragmentTag = MassageListFragment.TAG)
            )

        }

        cartItemAdapter.addAll(item1)
    }

    override fun onCartBarClick() {
        if (parentFragmentManager.findFragmentByTag(PaymentMethodFragment.TAG)== null){
            val fragment = PaymentMethodFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .add(R.id.fragmentContainer,fragment)
                .addToBackStack(PaymentMethodFragment.TAG)
                .commit()
        }
    }

    override fun onIncreaseMenuItemClicked(count: Int) {
        val a=10
    }

    override fun onDecreaseMenuItemClicked(count: Int) {
        val a=10
    }
}