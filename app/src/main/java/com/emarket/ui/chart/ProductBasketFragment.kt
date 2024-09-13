package com.emarket.ui.chart

import android.os.Bundle
import android.view.View
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentProductBasketBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductBasketFragment :
    BaseFragment<FragmentProductBasketBinding>(FragmentProductBasketBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {


        }

    }}