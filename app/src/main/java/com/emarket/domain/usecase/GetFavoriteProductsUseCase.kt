package com.emarket.domain.usecase

import com.emarket.data.local.FavoriteItemEntity
import com.emarket.data.repository.ServiceRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(private val serviceRepositoryImpl: ServiceRepositoryImpl) {
    operator fun invoke(): Flow<List<FavoriteItemEntity>> {
        return serviceRepositoryImpl.getLocalFavoriteItems()
    }
}
