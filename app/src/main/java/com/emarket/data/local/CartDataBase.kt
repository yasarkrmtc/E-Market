package com.emarket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemEntity::class, FavoriteItemEntity::class], version = 2)
abstract class CartDataBase : RoomDatabase() {
    abstract fun productsDao(): CartDAO
    abstract fun favoriteDao(): FavoriteDAO
}
