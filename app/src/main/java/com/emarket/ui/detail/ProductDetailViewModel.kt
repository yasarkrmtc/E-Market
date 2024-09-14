package com.emarket.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emarket.data.remote.Product
import com.emarket.domain.usecase.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
) : ViewModel() {

    fun updateFavoriteStatus(product: Product) {
        viewModelScope.launch {
            product.isFavorite = !product.isFavorite
            updateFavoriteUseCase(product)
        }
    }
}
