package com.emarket.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.emarket.base.BaseFragment
import com.emarket.data.remote.Product
import com.emarket.databinding.FragmentProductDetailBinding
import com.emarket.ui.MainActivity
import com.emarket.utils.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment :
    BaseFragment<FragmentProductDetailBinding>(FragmentProductDetailBinding::inflate) {

    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product
        setupUI(product)
        setupListeners(product)
        observeDatabaseCounter()
        viewModel.getDataBaseItemCount()
    }

    private fun setupUI(product: Product) {
        with(binding) {
            detailTitle.text = product.name
            detailName.text = product.name
            detailPriceText.text = product.price
            detailDescription.text = product.description
            detailFavouriteStar.isSelected = product.isFavorite

            Glide.with(requireContext())
                .load(product.image)
                .into(detailImage)
        }
    }

    private fun setupListeners(product: Product) {
        with(binding) {
            detailBackButton.clickWithDebounce { findNavController().popBackStack() }

            detailFavouriteStar.setOnClickListener {
                toggleFavoriteStatus(product)
            }

            productDetailButton.setOnClickListener {
                addToBasket(product)
            }
        }
    }

    private fun toggleFavoriteStatus(product: Product) {
        product.isFavorite = !product.isFavorite
        binding.detailFavouriteStar.isSelected = product.isFavorite
        viewModel.updateFavoriteStatus(product)
    }

    private fun addToBasket(product: Product) {
        val updatedProduct = product.copy(totalOrder = product.totalOrder + 1)
        viewModel.updateDataBase(updatedProduct)
    }

    private fun observeDatabaseCounter() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.databaseCounter.collectLatest { count ->
                (requireActivity() as MainActivity).updateBottomNavigationBadge(count)
            }
        }
    }
}