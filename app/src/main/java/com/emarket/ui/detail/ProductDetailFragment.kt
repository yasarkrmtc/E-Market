package com.emarket.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.emarket.base.BaseFragment
import com.emarket.data.remote.Product
import com.emarket.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment :
    BaseFragment<FragmentProductDetailBinding>(FragmentProductDetailBinding::inflate) {

    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.apply {
            detailName.text = product.name
            detailPriceText.text = product.price
            detailDescription.text = product.description

            Glide.with(requireContext())
                .load(product.image)
                .into(detailImage)

            // Set initial favorite state
            detailFavouriteStar.isSelected = product.isFavorite

            // Handle favorite state toggle
            detailFavouriteStar.setOnClickListener {
                product.isFavorite = !product.isFavorite
                detailFavouriteStar.isSelected = product.isFavorite
                // Update favorite status in ViewModel
                viewModel.updateFavoriteStatus(product)
            }

            productDetailButton.setOnClickListener {
                // Handle add to cart action
            }
        }
    }
}
