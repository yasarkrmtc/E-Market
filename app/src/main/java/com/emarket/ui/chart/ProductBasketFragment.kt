package com.emarket.ui.chart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.emarket.base.BaseFragment
import com.emarket.data.remote.Product
import com.emarket.databinding.FragmentProductBasketBinding
import dagger.hilt.android.AndroidEntryPoint
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

        // Handle complete button click
        binding.completeButton.setOnClickListener {
            if (basketAdapter.currentList.isNotEmpty()) {
                viewModel.clearDatabase()
                Toast.makeText(requireContext(), "Completed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No items to complete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = basketAdapter
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.localPrice.collect { price ->
                    binding.priceText.text = price
                }
            }
        }
    }
}
