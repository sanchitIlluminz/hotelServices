package com.illuminz.application.ui.bookTable

import android.os.Bundle
import android.view.View
import com.core.extensions.dpToPx
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.SliderLayoutManager
import com.illuminz.application.R
import com.illuminz.application.ui.bookTable.items.BookingTimeItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_booking_time.*


class BookingTimeFragment : DaggerBaseFragment() {

    companion object {
        private const val TAG = "BookingFragment"

        private const val TAB_NUMBER = "TAB_NUMBER"

        fun newInstance(position: Int): BookingTimeFragment {
            val args = Bundle()
            args.putInt(TAB_NUMBER, position)
            val fragment = BookingTimeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_booking_time

    private lateinit var hourAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var minutesAdapter: GroupAdapter<GroupieViewHolder>

    private var selectedHourPosition = 0
    private var selectedMinutePosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setBasicData()
    }


    private fun initialise() {
        when (requireArguments().getInt(TAB_NUMBER)) {
            0 -> {
                tvTime.setText(getString(R.string.am))
            }
            else -> {
                tvTime.setText(getString(R.string.pm))
            }
        }
    }

    private fun setBasicData() {

        val padding = requireContext().dpToPx((192 / 2) - (64 / 2))

        hourAdapter = GroupAdapter()
        hourAdapter.setOnItemClickListener { _, view ->
            rvHour.smoothScrollToPosition(rvHour.getChildLayoutPosition(view))
        }
        rvHour.adapter = hourAdapter
        rvHour.clipToPadding = false
        rvHour.setPadding(0, padding, 0, padding)
        rvHour.layoutManager = SliderLayoutManager(requireContext()).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    selectedHourPosition = layoutPosition
                }
            }
        }

        minutesAdapter = GroupAdapter()
        minutesAdapter.setOnItemClickListener { _, view ->
            rvMin.smoothScrollToPosition(rvMin.getChildLayoutPosition(view))
        }
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

        val hrList1 = arrayListOf(9, 10, 11)
        val hrList2 = arrayListOf(1, 2, 3)
        val hrList3 = arrayListOf(8, 9, 10)

        when (arguments?.getInt(TAB_NUMBER)) {
            0 -> {
                for (i in 1..hrList1.size) {
                    val hrItem = BookingTimeItem(hrList1.get(i - 1))
                    hourAdapter.add(hrItem)
                }
            }

            1 -> {
                for (i in 1..hrList2.size) {
                    val hrItem = BookingTimeItem(hrList2.get(i - 1))
                    hourAdapter.add(hrItem)
                }
            }

            else -> {
                for (i in 1..hrList3.size) {
                    val hrItem = BookingTimeItem(hrList3.get(i - 1))
                    hourAdapter.add(hrItem)
                }
            }
        }

        for (i in 0..60) {
            val minItem = BookingTimeItem(min)
            minutesAdapter.add(minItem)
            min = min.plus(1)
        }

    }
}