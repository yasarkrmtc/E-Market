package com.emarket.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentProductListingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListingFragment :
    BaseFragment<FragmentProductListingBinding>(FragmentProductListingBinding::inflate) {

    private val viewModel : ProductListingViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productAdapter = ProductListingAdapter()

        binding.rv.layoutManager = GridLayoutManager(context, 2)
        binding.rv.adapter = productAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts().collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }
        }
        productAdapter.itemClick { product ->
            viewModel.updateDataBase(product)
            // Navigate to ProductDetailFragment
            val action = ProductListingFragmentDirections
                .actionProductListingFragmentToProductDetailFragment(product)
            findNavController().navigate(action)
        }

        productAdapter.favoriteClick {
            Log.e("qqqqq","1111111")
            viewModel.updateFavorite(it)

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
