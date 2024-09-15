package com.emarket.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.emarket.data.FilterCriteria
import com.emarket.data.remote.PagingDataSource
import com.emarket.data.remote.Product
import com.emarket.data.remote.ServiceInterface
import com.emarket.data.repository.ServiceRepositoryImpl
import com.emarket.domain.usecase.GetDataBaseItemCount
import com.emarket.domain.usecase.InsertDataBaseUseCase
import com.emarket.domain.usecase.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val serviceInterface: ServiceInterface,
    private val insertDataBaseUseCase: InsertDataBaseUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val repository: ServiceRepositoryImpl,
    private val getDataBaseItemCounter: GetDataBaseItemCount

) : ViewModel() {
    init {
        savedStateHandle["PAGING_ID"] = 1
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: MutableStateFlow<String> = _searchQuery

    private val _databaseCounter = MutableStateFlow(0)
    val databaseCounter: MutableStateFlow<Int> = _databaseCounter

    private val _selectedBrands = MutableStateFlow("")
    private val _selectedModels = MutableStateFlow("")
    private val _selectedSortBy = MutableStateFlow("")

    val productsFlow: Flow<PagingData<Product>> = combine(searchQuery, _selectedBrands, _selectedModels, _selectedSortBy) { query, brands, models, sortBy ->
        FilterCriteria(query, brands, models, sortBy)
    }.flatMapLatest { criteria ->
        getProducts(criteria.query, criteria.brands, criteria.models, criteria.sortBy)
    }.cachedIn(viewModelScope)


    private fun getProducts(query: String, selectedBrands: String, selectedModels: String, selectedSortBy: String): Flow<PagingData<Product>> {
        return Pager(
            PagingConfig(
                pageSize = 4,
                initialLoadSize = 4,
                enablePlaceholders = true,
                prefetchDistance = 1
            )
        ) {
            PagingDataSource(serviceInterface, selectedBrands, selectedModels, selectedSortBy) // Modify PagingDataSource constructor if needed
        }.flow.map { pagingData ->
            pagingData.map { product ->
                val favoriteProduct = repository.getFavoriteProductById(product.id)
                if (favoriteProduct != null) {
                    product.isFavorite = true
                }
                product
            }.filter { product ->
                // Filter products based on search query
                product.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun updateSelectedSortBy(sortBy: String) {
        _selectedSortBy.value = sortBy
    }

    fun updateDataBase(item: Product) {
        viewModelScope.launch {
            insertDataBaseUseCase(item)
        }
        getDataBaseItemCount()
    }

    fun updateFavorite(item: Product) {
        viewModelScope.launch {
            updateFavoriteUseCase(item)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSelectedBrands(brands: String) {
        _selectedBrands.value = brands
    }

    fun updateSelectedModels(models: String) {
        _selectedModels.value = models
    }
    fun getDataBaseItemCount(){
        viewModelScope.launch {
           getDataBaseItemCounter().collect{
               _databaseCounter.value = it
           }
        }
    }
}

