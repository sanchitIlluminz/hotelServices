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

class SearchDialogFragment(var callback: Callback) : DaggerAppCompatDialogFragment(),
    FoodItem.Callback {
    companion object {
        const val TAG = "SearchDialogFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(
            requireParentFragment(),
            viewModelFactory
        )[FoodViewModel::class.java]
    }

//    private val isRestaurantOpen by lazy {
//        restaurantDetailsViewModel.isRestaurantOpen()
//    }

    private val displayedSearchItems by lazy { mutableListOf<FoodItem>() }

//    private val noResultFoundItem by lazy { SearchNoResultFoundItem() }

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
        searchAdapter.spanCount = 12
        val layoutManager = GridLayoutManager(requireActivity(), searchAdapter.spanCount)
        layoutManager.spanSizeLookup = searchAdapter.spanSizeLookup
        rvSearchResult.layoutManager = layoutManager
        rvSearchResult.addItemDecoration(
            InsetItemDecoration(
                Color.TRANSPARENT,
                requireActivity().dpToPx(20),
                "AppConstants.INSET_KEY",
                "AppConstants.INSET_VALUE"
            )
        )
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

//        searchView.queryHint =
//            getString(R.string.restaurant_details_search_label_search_in_with_name,
//                restaurantDetailsViewModel.getRestaurantName())
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
//                        searchAdapter.add(LoadingItem())
                    }

                    Status.SUCCESS -> {
                        val searchItems = resource.data
                        if (searchItems == null) {
                            displayedSearchItems.clear()
                            searchAdapter.clear()
                        } else {
                            /* val menuItems = searchMenuItems.map {
                                 RestaurantDetailsMenuItem(it,
                                     this@RestaurantDetailsSearchDialogFragment,
                                     isRestaurantOpen)
                             }*/

                            displayedSearchItems.clear()
                            searchAdapter.clear()

                            resource.data?.forEach {
                                searchAdapter.add(
                                    FoodItem(
                                        serviceCategoryItem = it,
                                        hideThumbnail = true,
                                        callback = this
                                    )
                                )
                            }
                            /*      if (menuItems.isNotEmpty()) {
                                      displayedMenuItems.addAll(menuItems)
                                      searchAdapter.add(DividerItem(requireContext().dpToPx(16)))
                                      searchAdapter.addAll(menuItems)
                                  } else {
                                      searchAdapter.add(noResultFoundItem)
                                  }*/
                        }
                    }

                    Status.ERROR -> {
                        val error = resource.error
                        if (error is AppError.ApiFailure && error.throwable !is CancellationException) {
                            displayedSearchItems.clear()
                            searchAdapter.clear()
//                            searchAdapter.add(noResultFoundItem)
                        }
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