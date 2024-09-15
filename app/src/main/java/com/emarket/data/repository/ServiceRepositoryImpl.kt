package com.emarket.data.repository

import android.util.Log
import com.emarket.data.local.CartDAO
import com.emarket.data.local.FavoriteDAO
import com.emarket.data.local.FavoriteItemEntity
import com.emarket.data.local.ItemEntity
import com.emarket.data.remote.Product
import com.emarket.data.remote.ServiceInterface
import com.emarket.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceInterface: ServiceInterface, private val cartDAO: CartDAO,private val favoriteDAO: FavoriteDAO
) : ServiceRepository {

    override suspend fun getTotalPrice(): Flow<Double?> = cartDAO.getTotalPrice()

    override suspend fun getLocalItems(): Flow<List<ItemEntity>> {
        return cartDAO.getAllItems()
    }

    override suspend fun getLocalItem(id: String): ItemEntity? {
        return cartDAO.getItemById(id)
    }
    override suspend fun deleteAllItems() {
        return cartDAO.deleteAll()
    }
    fun getLocalFavoriteItems(): Flow<List<FavoriteItemEntity>> {
        return favoriteDAO.getAllItems()
    }
    suspend fun getFavoriteProductById(productId: String): FavoriteItemEntity? {
        return favoriteDAO.getItemById(productId)
    }


    override suspend fun updateFavoriteUseCase(item: Product) {
        Log.e("qqqqq",item.isFavorite.toString())

        if (!item.isFavorite){
            favoriteDAO.delete(
                FavoriteItemEntity(
                    id = item.id,
                    createdAt = item.createdAt,
                    name = item.name,
                    image = item.image,
                    price = item.price,
                    description = item.description,
                    model = item.model,
                    brand = item.brand,
                    isFavorite = false

                )
            )
        }else{
            favoriteDAO.insert(
                FavoriteItemEntity(
                    id = item.id,
                    createdAt = item.createdAt,
                    name = item.name,
                    image = item.image,
                    price = item.price,
                    description = item.description,
                    model = item.model,
                    brand = item.brand,
                    isFavorite = true

                )
            )
        }
    }

    override suspend fun getItemsCount(): Flow<Int> {
        return cartDAO.getItemsCount()
    }

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