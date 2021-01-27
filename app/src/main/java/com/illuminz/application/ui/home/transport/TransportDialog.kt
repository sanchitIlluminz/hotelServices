package com.illuminz.application.ui.home.transport

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.core.ui.base.DaggerBaseDialogFragment
import com.illuminz.application.R
import kotlinx.android.synthetic.main.dialog_transport.*

class TransportDialog : DaggerBaseDialogFragment() {
    companion object {
        const val TAG = "TransportDialog"
        fun newInstance(): TransportDialog {
            return TransportDialog()
        }
    }

    override fun getLayoutResId(): Int = R.layout.dialog_transport

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        btRequest.setOnClickListener {
            dismiss()
        }


    }
}