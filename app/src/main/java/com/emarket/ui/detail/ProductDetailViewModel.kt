package com.emarket.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emarket.data.remote.Product
import com.emarket.domain.usecase.GetDataBaseItemCount
import com.emarket.domain.usecase.InsertDataBaseUseCase
import com.emarket.domain.usecase.UpdateFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val insertDataBaseUseCase: InsertDataBaseUseCase,
    private val getDataBaseItemCounter: GetDataBaseItemCount
) : ViewModel() {

    private val _databaseCounter = MutableStateFlow(0)
    val databaseCounter = _databaseCounter.asStateFlow()

    fun updateFavoriteStatus(product: Product) {
        viewModelScope.launch {
            updateFavoriteUseCase(product)
        }
    }

    fun updateDataBase(item: Product) {
        viewModelScope.launch {
            insertDataBaseUseCase(item)
            getDataBaseItemCount()
        }
    }

    fun getDataBaseItemCount() {
        viewModelScope.launch {
            getDataBaseItemCounter().collect {
                _databaseCounter.value = it
            }
        }
    }
}
