package com.emarket.domain.usecase


import com.emarket.data.repository.ServiceRepositoryImpl

import javax.inject.Inject

class GetLocalItemsUseCase @Inject constructor(private val serviceRepositoryImpl: ServiceRepositoryImpl) {
    suspend operator fun invoke()= serviceRepositoryImpl.getLocalItems()
}