package com.illuminz.application.ui.custom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.core.ui.base.DaggerBaseDialogFragment
import com.illuminz.application.R
import com.illuminz.application.ui.home.HomeFragment
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.android.synthetic.main.dialog_confirm.view.tvTitle
import kotlinx.android.synthetic.main.dialog_confirm.view.tvSubtitle

class ConfirmDialog(): DaggerBaseDialogFragment() {

    companion object{
        const val TAG = "ConfirmDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): ConfirmDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = ConfirmDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutResId(): Int = R.layout.dialog_confirm

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

        view.tvTitle.text = requireArguments().getString(KEY_TITLE)
        view.tvSubtitle.text = requireArguments().getString(KEY_SUBTITLE)

        val fragment = HomeFragment.newInstance()
        btnOkay.setOnClickListener {
            dismiss()

//            if (parentFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
//                    parentFragmentManager.beginTransaction().add(R.id.fragmentContainer,fragment).commit()
//            }

        }
    }

    interface Callback{
        fun openHomeFragment()
    }
}