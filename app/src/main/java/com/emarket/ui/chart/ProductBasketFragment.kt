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

        observeData()
        viewModel.getLocalItems()
        viewModel.getTotalPrice()
        viewModel.getDataBaseItemCount()
        initListeners()
    }

    private fun initListeners() {
        basketAdapter.onItemClick {
            viewModel.updateDataBase(
                Product(
                    id = it.id,
                    createdAt = it.createdAt,
                    name = it.name,
                    image = it.image,
                    price = it.price,
                    description = it.description,
                    model = it.model,
                    brand = it.brand,
                    totalOrder = it.totalOrder
                )
            )
        }

        binding.completeButton.setOnClickListener {
            if (basketAdapter.currentList.isNotEmpty()) {
                viewModel.clearDatabase()
                Toast.makeText(requireContext(), "Completed Successful", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), "No items to complete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = basketAdapter

        val spacing = resources.getDimensionPixelSize(R.dimen.size1)
        val itemDecoration = CustomAdaptiveDecoration(
            context = requireContext(),
            spanCount = 1,
            spacingHorizontal = spacing,
            spacingVertical = spacing,
            includeEdge = true
        )
        binding.recyclerView.addItemDecoration(itemDecoration)
    }


    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { items ->
                if (items.isNotEmpty()) {
                    basketAdapter.submitList(items)
                } else {
                    basketAdapter.submitList(listOf())
                }
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
}
