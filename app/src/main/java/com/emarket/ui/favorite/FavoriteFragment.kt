package com.emarket.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emarket.R
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentFavoriteBinding
import com.emarket.ui.home.ProductListingAdapter
import com.emarket.utils.CustomAdaptiveDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeFavoriteProducts()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(
            favoriteList = listOf(), // Initially empty list
            onDeleteClick = { product ->
                viewModel.deleteFavorite(product)
            }
        )

        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1) // Set columns for grid
        binding.favoriteRecyclerView.adapter = favoriteAdapter

        val spacing = resources.getDimensionPixelSize(R.dimen.size1)
        val itemDecoration = CustomAdaptiveDecoration(
            context = requireContext(),
            spanCount = 1,
            spacingHorizontal = spacing,
            spacingVertical = spacing,
            includeEdge = true
        )
        binding.favoriteRecyclerView.addItemDecoration(itemDecoration)
    }

    private fun observeFavoriteProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getFavoriteProducts().collectLatest { favoriteList ->
                favoriteAdapter.updateList(favoriteList)
            }
        }
    }
}

