package com.illuminz.application.ui.roomcleaning

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R
import com.illuminz.application.ui.roomcleaning.items.PopupCleaningItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import kotlinx.android.synthetic.main.fragment_room_cleaning.*


class RoomCleaningFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "RoomCleaningFragment"
        fun newInstance(): RoomCleaningFragment {
            return RoomCleaningFragment()
        }
    }

    private val scheduleTimings = listOf("10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM")
    private lateinit var cleaningPopup: PopupWindow

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
//                    spnSelectedTime.isEnabled = false
                }
                else -> {
                    tvSelectedTimeLabel.setTextColor(resources.getColor(R.color.sub_heading_text_color_1))
                    tvSelectedTime.setTextColor(resources.getColor(R.color.text_color_2))
                    tvSelectedTime.isEnabled = true
//                    spnSelectedTime.isEnabled = true
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

//            spnSelectedTime.performClick()
//            spnSelectedTime.visible()

            cleaningPopup = showPopupWindow()
            cleaningPopup.run {
                isOutsideTouchable = true
                isFocusable = true
                setBackgroundDrawable(ColorDrawable(Color.WHITE))
                showAsDropDown(tvSelectedTime)
            }

        }
//        spnSelectedTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                tvSelectedTime.setText(spnSelectedTime.selectedItem.toString())
//                spnSelectedTime.gone()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                spnSelectedTime.gone()
//            }
//        }

        btOrderNow.setOnClickListener {
            showConfirmationDialog(
                "Request submitted",
                "Our cleaning staff will arrive within 20 minutes."
            )
        }
    }

    private fun showConfirmationDialog(title: String, subtitle: String) {
        val dialog = context?.let { Dialog(it) }

        dialog?.run {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            val contentView = View.inflate(requireContext(), R.layout.dialog_confirm, null)
            setContentView(contentView)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.WHITE))
                setGravity(Gravity.CENTER)
            }

            contentView.tvTitle.text = title
            contentView.tvSubtitle.text = subtitle

            btnOkay.setOnClickListener {
                dismiss()
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            show()
        }
    }

    private fun initialise() {
//        val adapter =
//            ArrayAdapter(requireContext(), R.layout.spinner_item, R.id.tvTime, scheduleTimings)
//
//        spnSelectedTime?.adapter = adapter
//        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
    }


    fun showPopupWindow(): PopupWindow {

        val view = View.inflate(requireContext(), R.layout.popup_cleaning_time, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvPopupCleaning)

        val adapter:GroupAdapter<GroupieViewHolder> = GroupAdapter()

        recyclerView.adapter = adapter

        scheduleTimings.forEach {
            val popItem = PopupCleaningItem(it)
            adapter.add(popItem)
        }

        adapter.setOnItemClickListener { item, view ->
            if (item is PopupCleaningItem){
                tvSelectedTime.text = item.time
                cleaningPopup.let {
                    if(it.isShowing){
                        it.dismiss()
                    }
                }
            }
        }

        return PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)


//        // inflate the layout of the popup window
//        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
//        val popupView: View = inflater?.inflate(R.layout.popup_cleaning_time, null)
//
//        // create the popup window
//        val width = LinearLayout.LayoutParams.WRAP_CONTENT
//        val height = LinearLayout.LayoutParams.WRAP_CONTENT
//        val focusable = true // lets taps outside the popup also dismiss it
//        val popupWindow = PopupWindow(popupView, width, height, focusable)
//
//        // show the popup window
//        // which view you pass in doesn't matter, it is only used for the window tolken
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
//
//        // dismiss the popup window when touched
//        popupView.setOnTouchListener { v, event ->
//            popupWindow.dismiss()
//            true
//        }
    }
}