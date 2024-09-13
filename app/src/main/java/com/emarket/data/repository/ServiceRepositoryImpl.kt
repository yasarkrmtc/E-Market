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

    override suspend fun getProducts(): Flow<Response<List<Product>>> {
        return callbackFlow {
            val call = serviceInterface.fetchProduct()
            trySend(Response.Loading)
            call.enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: retrofit2.Response<List<Product>>
                ) {
                    val itemResponses = response.body() ?: emptyList()
                    trySend(Response.Success(itemResponses))
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    trySend(
                        Response.Error(
                            _code = t.hashCode().toString(), _message = t.localizedMessage
                        )
                    )
                }

            })
            awaitClose()
        }
    }
}