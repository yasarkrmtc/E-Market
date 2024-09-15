package com.emarket.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emarket.data.local.FavoriteItemEntity
import com.emarket.data.remote.Product
import com.emarket.domain.usecase.GetFavoriteProductsUseCase
import com.emarket.domain.usecase.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
) : ViewModel() {

    fun getFavoriteProducts(): Flow<List<FavoriteItemEntity>> {
        return getFavoriteProductsUseCase()
    }

    fun deleteFavorite(product: FavoriteItemEntity) {
        viewModelScope.launch {
            updateFavoriteUseCase(product.toProduct(isFavorite = false))
        }
    }

    private fun FavoriteItemEntity.toProduct(isFavorite: Boolean) = Product(
        id = id,
        createdAt = createdAt,
        name = name,
        image = image,
        price = price,
        description = description,
        model = model,
        brand = brand,
        isFavorite = isFavorite
    )
}
