package com.emarket.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentProductListingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListingFragment :
    BaseFragment<FragmentProductListingBinding>(FragmentProductListingBinding::inflate) {

    private val viewModel : ProductViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productAdapter = ProductAdapter()

        binding.rv.layoutManager = GridLayoutManager(context, 2)
        binding.rv.adapter = productAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts().collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }
        }
        productAdapter.itemClick { product ->
            viewModel.updateDataBase(product)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            productAdapter.loadStateFlow.collectLatest { loadStates ->
                val isLoading = loadStates.source.refresh is androidx.paging.LoadState.Loading
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

                val isError = loadStates.source.refresh is androidx.paging.LoadState.Error
                if (isError) {
                    Toast.makeText(
                        requireContext(),
                        "Veriler yüklenirken hata oluştu!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }}
