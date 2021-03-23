package com.illuminz.application.ui.bar

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import com.core.extensions.dpToPx
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.bar.items.*
import com.illuminz.application.ui.food.items.MenuDialogItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_menu.*
import kotlinx.android.synthetic.main.fragment_drink.*
import kotlinx.android.synthetic.main.fragment_drink.toolbar

class DrinksFragment : DaggerBaseFragment(), DrinkItem.Callback, DrinkDialog.Callback,
    CartBarView.Callback {
    companion object {
        const val TAG = "DrinksFragment"
        fun newInstance(): DrinksFragment {
            return DrinksFragment()
        }
    }

    private lateinit var barAdapter: GroupAdapter<GroupieViewHolder>
    private var itemSelected: Int? = 0
    private var totalPrice: Double = 0.00

    override fun getLayoutResId(): Int = R.layout.fragment_drink

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        btnMenu.setOnClickListener {
            showMenuDialog()
        }
//        barAdapter.setOnItemClickListener { item, view ->
//            if (item is DrinkItem){
//                DrinkDialog.newInstance(item.drinkName,item.drinkVol1,item.drinkVol2,item.price1,item.price2,item.quantity)
//                    .show(childFragmentManager,DrinkDialog.TAG)
//            }
//
//        }
    }

    private fun initialise() {

        cartBarView.setButtonText(getString(R.string.view_cart))
        cartBarView.setItemPrice(totalPrice = 820.00, items = 4)
        cartBarView.setCallback(this)

        barAdapter = GroupAdapter()
        rvBar.adapter = barAdapter

        val item = DrinkTypeItem(drinkType = "Beer", quantity = 90)
        val item1 = BarItem(drinkType = "Draught Beer", qt1 = "330ml")

        val item2 = listOf(
            DrinkItem(
                drinkName = "Kingfisher", price1 = 199.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Edelweiss", price1 = 299.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Hoegarden", price1 = 399.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Stella", price1 = 499.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Kingfisher", price1 = 599.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            )
        )

        val item3 = BarItem(drinkType = "Bottled Beer", qt1 = "Pint")

        val item4 = listOf(
            DrinkItem(
                drinkName = "Kingfisher Premium", price1 = 199.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Edelweiss Ultra", price1 = 299.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Hoegarden Ultra Max", price1 = 399.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Heineken", price1 = 499.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Kingfisher Buzz", price1 = 599.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            )
        )

        val item5 = BarItem(drinkType = "Craft Beer", qt1 = "330ml")

        val item6 = listOf(
            DrinkItem(
                drinkName = "Bira Blonde", price1 = 199.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Bira White ", price1 = 299.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            ),
            DrinkItem(
                drinkName = "Hoegarden Ultra Max", price1 = 399.00, price2 = 999.00, drinkType = "",
                drinkVol1 = "330ml", drinkVol2 = "1.5ltr", callback = this
            )
        )

        barAdapter.add(item)
        barAdapter.add(item1)
        barAdapter.addAll(item2)
        barAdapter.add(item3)
        barAdapter.addAll(item4)
        barAdapter.add(item5)
        barAdapter.addAll(item6)


    }

//    override fun addDrink() {
//        DrinkDialog.newInstance(item.drinkName,item.drinkVol1,item.drinkVol2,item.price1,item.price2,item.quantity)
//            .show(childFragmentManager,DrinkDialog.TAG)
//    }

    override fun setQuantity(quantity: Int?) {
    }


    override fun addDrink(drinkList: ArrayList<DrinkOrder>, drinkName: String) {
        DrinkDialog.newInstance(drinkList, drinkName)
            .show(childFragmentManager, DrinkDialog.TAG)
    }

    override fun onCartBarClick() {
//        if (parentFragmentManager.findFragmentByTag(CartFragment.TAG) == null) {
//            val fragment = CartFragment.newInstance(TAG)
//            parentFragmentManager.beginTransaction()
//                .setCustomAnimations(AnimationDirection.End)
//                .add(R.id.fragmentContainer, fragment)
//                .addToBackStack(CartFragment.TAG)
//                .commit()
//        }
    }

    private fun showMenuDialog() {
        val menuList = listOf(
            MenuDialogItem(title = "Draught Beer", number = 6),
            MenuDialogItem(title = "Bottled Beer", number = 8),
            MenuDialogItem(title = "Craft Beer", number = 12)
        )
        val dialog = context?.let { Dialog(it) }

        var menuAdapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_menu)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.BOTTOM or Gravity.END)
                attributes.x = context.dpToPx(24) // left margin
                attributes.y = context.dpToPx(140) // bottom margin
            }

            rvDialogMenu.adapter = menuAdapter
//
            menuAdapter.addAll(menuList)

            menuAdapter.setOnItemClickListener { item, view ->
                dialog.dismiss()
            }
        }

        dialog?.show()
    }
}