package com.emarket.data.repository

import com.emarket.core.Response
import com.emarket.data.local.CartDAO
import com.emarket.data.remote.Product
import com.emarket.data.remote.ServiceInterface
import com.emarket.domain.repository.ServiceRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceInterface: ServiceInterface, private val cartDAO: CartDAO
) : ServiceRepository {


}