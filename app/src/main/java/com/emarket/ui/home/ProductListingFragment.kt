package com.emarket.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentProductListingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListingFragment :
    BaseFragment<FragmentProductListingBinding>(FragmentProductListingBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
    }
    }