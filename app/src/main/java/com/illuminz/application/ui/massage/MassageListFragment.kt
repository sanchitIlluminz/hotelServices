package com.illuminz.application.ui.massage

import android.os.Bundle
import android.view.View
import com.core.extensions.setCustomAnimations
import com.core.ui.base.DaggerBaseFragment
import com.core.utils.AnimationDirection
import com.illuminz.application.R
import com.illuminz.application.ui.custom.CartBarView
import com.illuminz.application.ui.food.CartFragment
import com.illuminz.application.ui.massage.items.MassageItems
import com.illuminz.application.ui.massage.items.MassageTitleItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_massage_list.*
import kotlinx.android.synthetic.main.fragment_massage_list.cartBarView
import kotlinx.android.synthetic.main.fragment_massage_list.toolbar

class MassageListFragment : DaggerBaseFragment(), MassageItems.Callback, CartBarView.Callback {
    companion object {
        const val TAG = "MassageListFragment"
        fun newInstance(): MassageListFragment{
            return MassageListFragment()
        }
    }

    private lateinit var massageAdapter: GroupAdapter<GroupieViewHolder>
    private var totalPrice:Double = 0.00

    override fun getLayoutResId(): Int = R.layout.fragment_massage_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setListeners()
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }




    }

    private fun initialise() {
        toolbar.subtitle = "Timings: 10:30 AM - 8:00 PM"

        cartBarView.setButtonText(getString(R.string.view_cart))
        cartBarView.setItemPrice(totalPrice = 820.00,items = 4)

        cartBarView.setCallback(this)

        massageAdapter = GroupAdapter()
        rvMassage.adapter = massageAdapter

        val item = MassageTitleItem(getString(R.string.spa_amp_massage))

        val item1 = listOf(  MassageItems(title = "Choice of Full Body Massage (50 mins) + Shower (10 mins)",
                                         personCount = 2 , duration = "60min",
                                         quantity = 2, callback = this   ),

                            MassageItems(title = "Ayurvedic Shirodhara Treatment (60 mins)",
                                         personCount = 2 , duration = "60min", callback = this  ),

                            MassageItems(title = "Aroma Full Body Massage (45 mins) + Shower (15 mins)",
                                         personCount = 2 , duration = "60min", callback = this,
                                         quantity = 3),

                            MassageItems(title = "Foot Reflexology/ Balinese Back Massage/ Shiroabhiyangam\n" + "(30 mins)",
                                         personCount = 2 , duration = "60min", callback = this  ),

                            MassageItems(title = "Ayurvedic Potli Full Body Massage (70 mins) + Shower (15 mins)",
                                         personCount = 2 , duration = "60min", callback = this  ),

                            MassageItems(title = "Choice of Full Body Massage (50 mins) + Shower (10 mins)",
                                         personCount = 2 , duration = "60min",
                                         quantity = 2, callback = this   ),

                            MassageItems(title = "Ayurvedic Shirodhara Treatment (60 mins)",
                                         personCount = 2 , duration = "60min", callback = this,lastItem = true  )
        )
        massageAdapter.add(item)
        massageAdapter.addAll(item1)
    }

    override fun onIncreaseMenuItemClicked(count: Int) {
        val a = 10
    }

    override fun onDecreaseMenuItemClicked(count: Int) {
        val a = 10

    }

    override fun onCartBarClick() {
        if (parentFragmentManager.findFragmentByTag(CartFragment.TAG)== null){
            val fragment = CartFragment.newInstance(TAG)
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(AnimationDirection.End)
                .add(R.id.fragmentContainer,fragment)
                .addToBackStack(CartFragment.TAG)
                .commit()
        }
    }
}