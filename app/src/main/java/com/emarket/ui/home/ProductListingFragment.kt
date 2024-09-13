package com.emarket.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
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
            Log.e("qqqqqq", "1111")

        val productAdapter = ProductAdapter()
        binding.rv.adapter = productAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts().collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }
        }

    }
    }