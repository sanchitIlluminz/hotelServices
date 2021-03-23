package com.illuminz.application.ui.massage

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
import com.illuminz.application.R
import com.illuminz.application.ui.food.NoResultFoundItem
import com.illuminz.application.ui.massage.items.MassageItem
import com.illuminz.data.models.common.Status
import com.illuminz.error.AppError
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.fragment_search_dialog.*
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SearchMassageDialogFragment(
    var callback: Callback
) : DaggerAppCompatDialogFragment(), MassageItem.Callback {

    companion object {
        const val TAG = "SearchMassageDialogFragment"
    }

    private lateinit var searchAdapter: GroupAdapter<GroupieViewHolder>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment(), viewModelFactory)[MassageViewModel::class.java]
    }

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
        return layoutInflater.inflate(R.layout.fragment_search_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                viewModel.searchItems(newText)
                return true
            }
        })

        btnBack.setOnClickListener {
            dismiss()
        }

        observeChanges()
    }

    private fun observeChanges() {
        viewModel.getSearchObserver().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    searchAdapter.clear()
                }

                Status.SUCCESS -> {
                    searchAdapter.clear()
                    if (resource.data?.isEmpty() == true){
                        searchAdapter.add(NoResultFoundItem())
                    }else{
                        resource.data?.forEach { serviceCategoryItem ->
                            searchAdapter.add(
                                MassageItem(
                                    serviceCategoryItem = serviceCategoryItem,
                                    callback = this
                                )
                            )
                        }
                    }
                }

                Status.ERROR -> {
                    val error = resource.error
                    if (error is AppError.ApiFailure && error.throwable !is CancellationException)
                        searchAdapter.clear()
                }
            }
        })
    }

    interface Callback {
        fun onIncreaseSearchItemClicked(massageItem: MassageItem)
        fun onDecreaseSearchItemClicked(massageItem: MassageItem)
    }

    override fun onIncreaseMassageItemClicked(massageItem: MassageItem) {
        callback.onIncreaseSearchItemClicked(massageItem)
    }

    override fun onDecreaseMassageItemClicked(massageItem: MassageItem) {
        callback.onDecreaseSearchItemClicked(massageItem)
    }
}