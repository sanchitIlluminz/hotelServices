package com.illuminz.application.ui.food

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.di.ViewModelFactory
import com.core.extensions.dpToPx
import com.core.extensions.hideKeyboard
import com.core.extensions.showKeyboard
import com.core.utils.AppConstants
import com.core.utils.InsetItemDecoration
import com.illuminz.application.R
import com.illuminz.application.ui.food.items.FoodItem
import com.illuminz.data.models.common.Status
import com.illuminz.data.models.response.ServiceCategoryItemDto
import com.illuminz.error.AppError
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.fragment_search_dialog.*
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SearchFoodDialogFragment(var callback: Callback) : DaggerAppCompatDialogFragment(),
    FoodItem.Callback {
    companion object {
        const val TAG = "SearchFoodDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(
            requireParentFragment(),
            viewModelFactory
        )[FoodViewModel::class.java]
    }

    private val displayedSearchItems by lazy { mutableListOf<FoodItem>() }

    private lateinit var searchAdapter: GroupAdapter<GroupieViewHolder>

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

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
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
                viewModel.searchItems(newText)
                return true
            }
        })

        observeChanges()
    }

    private fun observeChanges() {
        viewModel.getSearchItemsObserver()
            .observe(this, Observer { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        displayedSearchItems.clear()
                        searchAdapter.clear()
                    }

                    Status.SUCCESS -> {
                        val searchItems = resource.data
                        if (searchItems == null) {
                            displayedSearchItems.clear()
                            searchAdapter.clear()
                        } else {
                            displayedSearchItems.clear()
                            searchAdapter.clear()

                            if (resource.data?.isEmpty() == true) {
                                searchAdapter.add(NoResultFoundItem())
                            } else {
                                resource.data?.forEach {
                                    searchAdapter.add(
                                        FoodItem(
                                            serviceCategoryItem = it,
                                            hideThumbnail = true,
                                            callback = this
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Status.ERROR -> {
                    }
                }
            })
    }


    interface Callback {
        fun onIncreaseSearchItemClicked(serviceCategoryItem: ServiceCategoryItemDto)
        fun onDecreaseSearchItemClicked(serviceCategoryItem: ServiceCategoryItemDto)
    }

    override fun onIncreaseFoodItemClicked(foodItem: FoodItem) {
        callback.onIncreaseSearchItemClicked(foodItem.serviceCategoryItem)
    }

    override fun onDecreaseFoodItemClicked(foodItem: FoodItem) {
        callback.onDecreaseSearchItemClicked(foodItem.serviceCategoryItem)
    }
}