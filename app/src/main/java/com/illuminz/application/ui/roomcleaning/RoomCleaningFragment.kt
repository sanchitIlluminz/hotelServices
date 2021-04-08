package com.illuminz.application.ui.roomcleaning

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
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
                    tvSelectedTimeLabel.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                    tvSelectedTime.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                    tvSelectedTime.isEnabled = false
                }
                else -> {
                    tvSelectedTimeLabel.setTextColor(ContextCompat.getColor(requireContext(),R.color.sub_heading_text_color_1))
                    tvSelectedTime.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_color_2))
                    tvSelectedTime.isEnabled = true
                }
            }
        }

        tvSelectedTime.setOnClickListener {
            cleaningPopup = showPopupWindow()
            cleaningPopup.run {
                isOutsideTouchable = true
                isFocusable = true
                setBackgroundDrawable(ColorDrawable(Color.WHITE))
                showAsDropDown(tvSelectedTime)
            }
        }

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

    private fun initialise() {}

    fun showPopupWindow(): PopupWindow {
        val view = View.inflate(requireContext(), R.layout.popup_cleaning_time, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvPopupCleaning)

        val adapter: GroupAdapter<GroupieViewHolder> = GroupAdapter()
        recyclerView.adapter = adapter

        scheduleTimings.forEach {
            val popItem = PopupCleaningItem(it)
            adapter.add(popItem)
        }

        adapter.setOnItemClickListener { item, view ->
            if (item is PopupCleaningItem) {
                tvSelectedTime.text = item.time
                cleaningPopup.let {
                    if (it.isShowing) {
                        it.dismiss()
                    }
                }
            }
        }

        return PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}