package com.emarket.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentProductDetailBinding
import com.emarket.ui.MainActivity
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
        viewModel.getDataBaseItemCount()
        binding.apply {
<<<<<<< HEAD
=======
            detailBackButton.clickWithDebounce {
                findNavController().popBackStack()
            }

            detailTitle.text = product.name
>>>>>>> parent of fb93259 (general clean)
            detailName.text = product.name
            detailPriceText.text = product.price
            detailDescription.text = product.description

            Glide.with(requireContext())
                .load(product.image)
                .into(detailImage)

            detailFavouriteStar.isSelected = product.isFavorite

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.databaseCounter.collectLatest { count ->
                    (requireActivity() as MainActivity).updateBottomNavigationBadge(count)
                }
            }
            detailFavouriteStar.setOnClickListener {
                product.isFavorite = !product.isFavorite
                detailFavouriteStar.isSelected = product.isFavorite
                viewModel.updateFavoriteStatus(product)
            }

            productDetailButton.setOnClickListener {
<<<<<<< HEAD
                val newProduct = product.copy(totalOrder = product.totalOrder+1)
               viewModel.updateDataBase(newProduct)
=======
                val newProduct = product.copy(totalOrder = product.totalOrder + 1)
                viewModel.updateDataBase(newProduct)
>>>>>>> parent of fb93259 (general clean)
            }
        }
    }
}
