package com.illuminz.application.ui.laundry

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.core.di.ViewModelFactory
import com.core.extensions.hideKeyboard
import com.core.extensions.isNullOrZero
import com.core.extensions.showKeyboard
import com.core.utils.AppConstants
import com.illuminz.application.R
import com.illuminz.application.ui.food.NoResultFoundItem
import com.illuminz.application.ui.laundry.items.LaundryItem
import com.illuminz.data.models.common.Status
import com.illuminz.error.AppError
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.fragment_search_dialog.*
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SearchLaundryDialogFragment(
    var callback: Callback,
    private val laundryType:Int
) : DaggerAppCompatDialogFragment(), LaundryItem.Callback {

    companion object {
        const val TAG = "SearchLaundyDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[LaundryViewModel::class.java]
    }

    private lateinit var searchAdapter: GroupAdapter<GroupieViewHolder>
    private var searchLaundryType = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.TOP)
            attributes.windowAnimations = R.style.DialogSlideDown
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchLaundryType = laundryType
        searchView.showKeyboard()
        btnBack.setOnClickListener {
            dismiss()
        }

        searchAdapter = GroupAdapter()
        rvSearchResult.adapter = searchAdapter
        rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    searchView.clearFocus()
                    recyclerView.hideKeyboard()
                }
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchItems(newText,searchLaundryType)
                return true
            }

        })

        observeChanges()
    }

    private fun observeChanges() {
        viewModel.getSearchObserver().observe(this, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    searchAdapter.clear()
                }

                Status.SUCCESS -> {
                    searchAdapter.clear()
                    val searchItems = resource.data
                    if (searchItems?.isEmpty() == true){
                        searchAdapter.add(NoResultFoundItem())
                    }else{
                        searchItems?.forEach {
                            if (!it.ironingPrice.isNullOrZero()) {
                                searchAdapter.add(
                                    LaundryItem(
                                        serviceCategoryItem = it,
                                        laundryType = AppConstants.LAUNDRY_ONLY_IRON,
                                        callback = this
                                    )
                                )
                            } else {
                                searchAdapter.add(
                                    LaundryItem(
                                        serviceCategoryItem = it,
                                        laundryType = AppConstants.LAUNDRY_WASH_IRON,
                                        callback = this
                                    )
                                )
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    val error = resource.error
                    if (error is AppError.ApiFailure && error.throwable !is CancellationException) {
                        searchAdapter.clear()
                    }
                }
            }
        })
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }


    override fun onIncreaseLaundryItemClicked(laundryItem: LaundryItem) {
        viewModel.updateOriginalLaundryList(laundryItem)
        callback.onIncreaseSearchItemClicked(laundryItem)

    }

    override fun onDecreaseLaundryItemClicked(laundryItem: LaundryItem) {
        viewModel.updateOriginalLaundryList(laundryItem)
        callback.onDecreaseSearchItemClicked(laundryItem)
    }

    interface Callback {
        fun onIncreaseSearchItemClicked(laundryItem: LaundryItem)
        fun onDecreaseSearchItemClicked(laundryItem: LaundryItem)
    }
}