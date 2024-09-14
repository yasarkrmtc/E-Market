package com.emarket.data.repository

import com.emarket.data.local.CartDAO
import com.emarket.data.local.ItemEntity
import com.emarket.data.remote.Product
import com.emarket.data.remote.ServiceInterface
import com.emarket.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceInterface: ServiceInterface, private val cartDAO: CartDAO
) : ServiceRepository {
    override suspend fun insertDataBaseItem(item: Product) {
        if (item.totalOrder == 0) {
            cartDAO.delete(
                ItemEntity(
                    id = item.id,
                    createdAt = item.createdAt,
                    name = item.name,
                    image=item.image,
                    price = item.price,
                    totalOrder = 1,
                    description = item.description,
                    model = item.model,
                    brand = item.brand
                )
            )

        } else {
            val existingItem = cartDAO.getItemById(item.id)
            if (existingItem != null) {
                existingItem.totalOrder = item.totalOrder
                cartDAO.update(existingItem)
            } else {
                cartDAO.insert(
                    ItemEntity(
                        id = item.id,
                        createdAt = item.createdAt,
                        name = item.name,
                        image=item.image,
                        price = item.price,
                        totalOrder = item.totalOrder,
                        description = item.description,
                        model = item.model,
                        brand = item.brand
                    )
                )
            }
        }

    }

}