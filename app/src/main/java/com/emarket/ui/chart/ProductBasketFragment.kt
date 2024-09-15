package com.emarket.ui.chart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.emarket.R
import com.emarket.base.BaseFragment
import com.emarket.data.local.ItemEntity
import com.emarket.data.remote.Product
import com.emarket.databinding.FragmentProductBasketBinding
import com.emarket.ui.MainActivity
import com.emarket.utils.CustomAdaptiveDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductBasketFragment :
    BaseFragment<FragmentProductBasketBinding>(FragmentProductBasketBinding::inflate) {

    private val basketAdapter = ProductBasketAdapter()
    private val viewModel: ProductBasketViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeData()
        viewModel.apply {
            getLocalItems()
            getTotalPrice()
            getDataBaseItemCount()
        }
    }

    private fun setupListeners() {
        basketAdapter.setOnItemClickListener {
            viewModel.updateDataBase(it.toProduct())
        }

        binding.completeButton.setOnClickListener {
            if (basketAdapter.currentList.isNotEmpty()) {
                viewModel.clearDatabase()
                showToast("Completed")
            } else {
                showToast("No items to complete")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = basketAdapter
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

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { items ->
                basketAdapter.submitList(items)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.databaseCounter.collectLatest { count ->
                (requireActivity() as MainActivity).updateBottomNavigationBadge(count)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.localPrice.collect { price ->
                    binding.priceText.text = price
                }
            }
        }
    }

    private fun ItemEntity.toProduct(): Product {
        return Product(
            id = id,
            createdAt = createdAt,
            name = name,
            image = image,
            price = price,
            description = description,
            model = model,
            brand = brand,
            totalOrder = totalOrder
        )
    }
}
