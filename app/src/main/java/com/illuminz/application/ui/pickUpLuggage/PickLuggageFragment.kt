package com.illuminz.application.ui.pickUpLuggage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.core.ui.base.DaggerBaseFragment
import com.illuminz.application.R

class PickLuggageFragment : DaggerBaseFragment() {
    companion object {
        const val TAG = "PickLuggageFragment"

        fun newInstance(): PickLuggageFragment{
            val fragment = PickLuggageFragment()
            return fragment
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_pick_luggage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}