package com.emarket.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.emarket.R
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentProductListingBinding
import com.emarket.ui.MainActivity
import com.emarket.utils.CustomAdaptiveDecoration
import com.emarket.utils.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListingFragment :
    BaseFragment<FragmentProductListingBinding>(FragmentProductListingBinding::inflate) {

    private val productListingAdapter = ProductListingAdapter()
    private val viewModel: ProductListingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFilterListener()
        setupSearchListener()
        observeViewModel()
        observeAdapterLoadState()
    }

    private fun setupRecyclerView() {
        binding.rv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productListingAdapter
            addItemDecoration(createItemDecoration())
        }

        productListingAdapter.setItemClickListener {
            viewModel.updateDataBase(it)
        }

        productListingAdapter.setProductClickListener {
            val action = ProductListingFragmentDirections
                .actionProductListingFragmentToProductDetailFragment(it)
            findNavController().navigate(action)
        }

        productListingAdapter.setFavoriteClickListener {
            viewModel.updateFavorite(it)
        }
    }

    private fun createItemDecoration(): CustomAdaptiveDecoration {
        val spacing = resources.getDimensionPixelSize(R.dimen.size3)
        return CustomAdaptiveDecoration(
            context = requireContext(),
            spanCount = 2,
            spacingHorizontal = spacing,
            spacingVertical = spacing,
            includeEdge = true
        )
    }

    private fun setupFilterListener() {
        setFragmentResultListener("filterResult") { _, bundle ->
            val selectedBrands = bundle.getStringArrayList("selectedBrands") ?: listOf()
            val selectedModels = bundle.getStringArrayList("selectedModels") ?: listOf()
            val selectedSortBy = bundle.getString("selectedSortBy") ?: ""

            viewModel.updateSelectedBrands(selectedBrands.joinToString(separator = "|"))
            viewModel.updateSelectedModels(selectedModels.joinToString(separator = "|"))
            viewModel.updateSelectedSortBy(selectedSortBy)
        }

        binding.selectFilterText.clickWithDebounce {
            findNavController().navigate(R.id.action_productListingFragment_to_filtersFragment)
        }
    }

    private fun setupSearchListener() {
        binding.homeSearchbar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newText = s.toString()
                viewModel.updateSearchQuery(newText)
                if (newText.isEmpty()) {
                    binding.rv.scrollToPosition(0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsFlow.collectLatest { pagingData ->
                productListingAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.databaseCounter.collectLatest { count ->
                (requireActivity() as MainActivity).updateBottomNavigationBadge(count)
            }
        }
    }

    private fun observeAdapterLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            productListingAdapter.loadStateFlow.collectLatest { loadState ->
                val isLoadingInitial = loadState.refresh is LoadState.Loading
                binding.progressBar.isVisible = isLoadingInitial

                val isAppending = loadState.append is LoadState.Loading
                binding.itemProggress.isVisible = isAppending

                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && productListingAdapter.itemCount == 0
                binding.rv.isVisible = !isLoadingInitial && !isListEmpty

                if (isListEmpty && !isLoadingInitial) {
                    binding.progressBar.visibility = View.GONE
                } else if (isLoadingInitial) {
                    binding.progressBar.visibility = View.VISIBLE
                }

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.source.refresh as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "An error occurred while loading data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


}
