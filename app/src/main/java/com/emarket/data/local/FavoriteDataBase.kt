package com.emarket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteItemEntity::class], version = 1)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun productsDao(): FavoriteDAO
}