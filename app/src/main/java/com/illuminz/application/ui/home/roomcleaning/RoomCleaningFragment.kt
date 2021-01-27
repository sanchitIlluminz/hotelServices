package com.illuminz.application.ui.home.roomcleaning

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import kotlinx.android.synthetic.main.fragment_room_cleaning.*

class RoomCleaningFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "RoomCleaningFragment"
        fun newInstance(): RoomCleaningFragment {
            return RoomCleaningFragment()
        }
    }

    private val scheduleTimings = listOf("10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM")

    override fun getLayoutResId(): Int = R.layout.fragment_room_cleaning

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
    }

    private fun setListeners() {

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbNow -> {
                    tvSelectedTimeLabel.setTextColor(resources.getColor(R.color.colorPrimary))
                    tvSelectedTime.setTextColor(resources.getColor(R.color.colorPrimary))
                    tvSelectedTime.isEnabled = false
                    spnSelectedTime.isEnabled = false
                }
                else ->{
                    tvSelectedTimeLabel.setTextColor(resources.getColor(R.color.sub_heading_text_color_1))
                    tvSelectedTime.setTextColor(resources.getColor(R.color.sub_heading_text_color_1))
                    tvSelectedTime.isEnabled = true
                    spnSelectedTime.isEnabled = true
                }
            }
        }

        tvSelectedTime.setOnClickListener {

//            AppAlertDialogBuilder(requireContext(),lifecycle)
//                .setTitle(getString(R.string.choose_title))
//                .setItems(scheduleTimings){ _, position ->
//                    tvSelectedTime.setText(scheduleTimings.get(position))
//                }
//                .create()
//                .show()
//
//            view?.hideKeyboard()

            spnSelectedTime.performClick()
        }
        spnSelectedTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tvSelectedTime.setText(spnSelectedTime.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun initialise() {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.tvTime, scheduleTimings)

        spnSelectedTime?.adapter = adapter
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
    }
}