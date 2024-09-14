package com.emarket.domain.usecase

import com.emarket.data.remote.Product
import com.emarket.data.repository.ServiceRepositoryImpl
import javax.inject.Inject

class InsertDataBaseUseCase @Inject constructor(private val serviceRepositoryImpl: ServiceRepositoryImpl) {
    suspend operator fun invoke(item: Product)= serviceRepositoryImpl.insertDataBaseItem(item)
}