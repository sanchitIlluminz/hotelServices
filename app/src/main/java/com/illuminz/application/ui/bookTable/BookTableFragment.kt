package com.illuminz.application.ui.bookTable

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.core.extensions.dpToPx
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.SliderLayoutManager
import com.illuminz.application.R
import com.illuminz.application.ui.bookTable.items.BookingDateItem
import com.illuminz.application.ui.bookTable.items.BookingTimeItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.fragment_book_table.*
import kotlinx.android.synthetic.main.fragment_book_table.rvHour
import kotlinx.android.synthetic.main.fragment_book_table.rvMin


class BookTableFragment : DaggerBaseFragment() {

    companion object {
        const val TAG = "BookTableFragment"
        fun newInstance(): BookTableFragment {
            return BookTableFragment()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_book_table

    private lateinit var hourAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var minutesAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var calendarAdapter: GroupAdapter<GroupieViewHolder>

    private var selectedHourPosition = 0
    private var selectedMinutePosition = 0

    private var selectedHourItem:BookingTimeItem? = null
    private var selectedBookingDateItem: BookingDateItem? = null

    private lateinit var itemList: List<BookingDateItem>

    private var guestNumber:Int =1

    private lateinit var hrList1:ArrayList<Int>
    private lateinit var hrList2:ArrayList<Int>
    private lateinit var hrList3:ArrayList<Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
    }

    private fun initialise() {
        tvGuestNumber.text = guestNumber.toString()

        calendarAdapter = GroupAdapter()
        rvCalendar.adapter = calendarAdapter

        itemList = listOf(
            BookingDateItem(date = "25", day = "Thu", selected = true).also {
                selectedBookingDateItem = it
            },
            BookingDateItem(date = "26", day = "Fri"),
            BookingDateItem(date = "27", day = "Sat"),
            BookingDateItem(date = "28", day = "Sun"),
            BookingDateItem(date = "29", day = "Mon")
        )

        calendarAdapter.addAll(itemList)

        hrList1 = arrayListOf(9, 10, 11)
        hrList2 = arrayListOf(1, 2, 3)
        hrList3 = arrayListOf(8, 9, 10)

        hourAdapter = GroupAdapter()
        rvHour.adapter = hourAdapter

        val padding = requireContext().dpToPx((176 / 2) - (48 / 2))
        rvHour.clipToPadding = false
        rvHour.setPadding(0, padding, 0, padding)
        rvHour.layoutManager = SliderLayoutManager(requireContext()).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    selectedHourPosition = layoutPosition
                    if (selectedHourItem!=null && selectedHourItem!= hourAdapter.getItem(selectedHourPosition) as BookingTimeItem){
                        selectedHourItem?.selected = false
                        selectedHourItem?.notifyChanged()

                        selectedHourItem = hourAdapter.getItem(selectedHourPosition) as BookingTimeItem
                        selectedHourItem?.selected = true
                        selectedHourItem?.notifyChanged()
                    }else if (selectedHourItem==null){
                        selectedHourItem = hourAdapter.getItem(selectedHourPosition) as BookingTimeItem
                        selectedHourItem?.selected = true
                        selectedHourItem?.notifyChanged()
                    }
                }
            }
        }

        hourAdapter.clear()
        for (i in 1..hrList1.size) {
            val hrItem = BookingTimeItem(hrList1.get(i - 1))
            hourAdapter.add(hrItem)
        }

        minutesAdapter = GroupAdapter()
        rvMin.adapter = minutesAdapter
        rvMin.clipToPadding = false
        rvMin.setPadding(0, padding, 0, padding)
        rvMin.layoutManager = SliderLayoutManager(requireContext()).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    selectedMinutePosition = layoutPosition
                }
            }
        }

        var min = 0
        for (i in 0..60) {
            val minItem = BookingTimeItem(min)
            minutesAdapter.add(minItem)
            min = min.plus(1)
        }
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        btConfirm.setOnClickListener {
//            ConfirmDialog.newInstance(
//                getString(R.string.reservation_confirmed),
//                "7 Dec, 10:20 AM, 4 Guests"
//            )
//                .show(childFragmentManager, ConfirmDialog.TAG)

            showDialog( getString(R.string.reservation_confirmed),
                "7 Dec, 10:20 AM, 4 Guests")
        }

        calendarAdapter.setOnItemClickListener { item, _ ->
            if (item is BookingDateItem && item != selectedBookingDateItem) {
                item.selected = true
                item.notifyChanged()

                selectedBookingDateItem?.selected = false
                selectedBookingDateItem?.notifyChanged()
                selectedBookingDateItem = item
            }
        }

        ibMinus.setOnClickListener {
            if (guestNumber>1){
                guestNumber = guestNumber.minus(1)
                tvGuestNumber.setText(guestNumber.toString())
            }
        }

        ibPlus.setOnClickListener {
                guestNumber =guestNumber.plus(1)
                tvGuestNumber.setText(guestNumber.toString())

        }

        hourAdapter.setOnItemClickListener { _, view ->
            rvHour.smoothScrollToPosition(rvHour.getChildLayoutPosition(view))
        }

        minutesAdapter.setOnItemClickListener { _, view ->
            rvMin.smoothScrollToPosition(rvMin.getChildLayoutPosition(view))
        }
    }

    private fun setHourAdapterItems(hrList:ArrayList<Int>,timeZone:String){
        hourAdapter.clear()
        for (i in 1..hrList.size) {
            val hrItem = BookingTimeItem(hrList.get(i - 1))
            hourAdapter.add(hrItem)
        }
        rvHour.scrollToPosition(0)
        rvMin.scrollToPosition(0)

        if (timeZone==getString(R.string.am))
            tvTime.text = getString(R.string.am)
        else
            tvTime.text = getString(R.string.pm)
    }

    private fun showDialog(title: String, subtitle: String) {
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