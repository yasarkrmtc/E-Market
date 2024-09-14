package com.emarket.ui.chart

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.emarket.base.BaseFragment
import com.emarket.data.remote.Product
import com.emarket.databinding.FragmentProductBasketBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductBasketFragment :
    BaseFragment<FragmentProductBasketBinding>(FragmentProductBasketBinding::inflate) {

    private lateinit var basketAdapter: ProductBasketAdapter
    private val basketItems = mutableListOf<Product>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()


        basketAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        basketAdapter = ProductBasketAdapter(basketItems,
            onItemClick = { product ->
            },
            onQuantityChanged = { product, newQuantity ->

            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = basketAdapter
        }
    }
}
