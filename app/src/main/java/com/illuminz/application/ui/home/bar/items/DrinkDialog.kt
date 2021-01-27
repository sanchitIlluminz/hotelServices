package com.illuminz.application.ui.home.bar.items

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.core.ui.base.DaggerBaseDialogFragment
import com.illuminz.application.R
import com.illuminz.application.ui.custom.ConfirmDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_drink.*

class DrinkDialog(
): DaggerBaseDialogFragment(),DrinkDialogItem.Callback {

    companion object{
        const val TAG = "DrinkDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_DRINK_TYPE_1 = "KEY_DRINK_TYPE_1"
        private const val KEY_DRINK_TYPE_2 = "KEY_DRINK_TYPE_2"
        private const val KEY_DRINK_PRICE_1 = "KEY_PRICE_1"
        private const val KEY_DRINK_PRICE_2 = "KEY_PRICE_2"
        private const val KEY_QUANTITY ="KEY_QUANTITY"
        private const val KEY_DRINK_LIST ="KEY_DRINK_LIST"

        fun newInstance(
//            title: String, drink1: String, drink2: String, price1:String, price2: String, quantity: Int
            drinkList: ArrayList<DrinkOrder>, drinkName: String
        ): DrinkDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, drinkName)
//            args.putString(KEY_DRINK_TYPE_1, drink1)
//            args.putString(KEY_DRINK_TYPE_2, drink2)
//            args.putString(KEY_DRINK_PRICE_1, price1)
//            args.putString(KEY_DRINK_PRICE_2, price2)
//            args.putInt(KEY_QUANTITY,quantity)
            args.putParcelableArrayList(KEY_DRINK_LIST,drinkList)

            val fragment = DrinkDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private var quantity:Int? = 0

    private lateinit var drinkAdapter:GroupAdapter<GroupieViewHolder>
    override fun getLayoutResId(): Int = R.layout.dialog_drink


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setListeners()

    }

    private fun setData() {
        quantity = arguments?.getInt(KEY_QUANTITY)
        tvTitle.text = arguments?.getString(KEY_TITLE)
        val drinkList = arguments?.getParcelableArrayList<DrinkOrder>(KEY_DRINK_LIST)

        drinkAdapter = GroupAdapter()
        rvDrink.adapter = drinkAdapter

        drinkList?.forEach {
            val item = DrinkDialogItem(it)
            drinkAdapter.add(item)
        }

    }

    private fun setListeners() {
        btConfirm.setOnClickListener {
//            callback.setQuantity(quantity)
            dismiss()
        }
    }

    interface Callback{
        fun setQuantity(quantity:Int?=0)
    }

    override fun onIncreaseMenuItemClicked(count: Int) {
        val a =10
    }

    override fun onDecreaseMenuItemClicked(count: Int) {
        val a =10
    }
}