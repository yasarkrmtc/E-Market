package com.emarket.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.emarket.R
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentFavoriteBinding
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
        favoriteAdapter = FavoriteAdapter(onDeleteClick = viewModel::deleteFavorite)

        binding.favoriteRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = favoriteAdapter
            addItemDecoration(createItemDecoration())
        }
    }

    private fun createItemDecoration(): CustomAdaptiveDecoration {
        val spacing = resources.getDimensionPixelSize(R.dimen.size1)
        return CustomAdaptiveDecoration(
            context = requireContext(),
            spanCount = 1,
            spacingHorizontal = spacing,
            spacingVertical = spacing,
            includeEdge = true
        )
    }

    private fun observeFavoriteProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getFavoriteProducts().collectLatest { favoriteList ->
                favoriteAdapter.updateList(favoriteList)
            }
        }
    }
}
