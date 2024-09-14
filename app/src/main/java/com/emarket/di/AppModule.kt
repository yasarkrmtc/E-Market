package com.emarket.di

import android.content.Context
import androidx.room.Room
import com.emarket.data.local.CartDataBase
import com.emarket.data.local.FavoriteDAO
import com.emarket.data.local.FavoriteDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        CartDataBase::class.java,
        "CartDatabase"
    ).build()

    @Singleton
    @Provides
    fun provideProductsDao(database : CartDataBase) = database.productsDao()

    @Singleton
    @Provides
    fun provideFavoriteDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        FavoriteDataBase::class.java,
        "FavoriteDataBase"
    ).build()

    @Singleton
    @Provides
    fun provideFavoriteProductsDao(database : FavoriteDataBase) = database.productsDao()
}