package com.emarket.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emarket.data.local.ItemEntity
import com.emarket.data.remote.Product
import com.emarket.domain.usecase.DeleteAllItemsUseCase
import com.emarket.domain.usecase.GetDataBaseItemCount
import com.emarket.domain.usecase.GetLocalItemsUseCase
import com.emarket.domain.usecase.GetTotalPriceUseCase
import com.emarket.domain.usecase.InsertDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductBasketViewModel @Inject constructor(
    private val getLocalItemsUseCase: GetLocalItemsUseCase,
    private val getTotalPriceUseCase: GetTotalPriceUseCase,
    private val insertDataBaseUseCase: InsertDataBaseUseCase,
    private val deleteAllItemsUseCase: DeleteAllItemsUseCase,
    private val getDataBaseItemCounter: GetDataBaseItemCount
) : ViewModel() {

    private val _items = MutableStateFlow<List<ItemEntity>>(emptyList())
    val items: StateFlow<List<ItemEntity>> = _items.asStateFlow()

    private val _databaseCounter = MutableStateFlow(0)
    val databaseCounter: StateFlow<Int> = _databaseCounter.asStateFlow()

    private val _localPrice = MutableStateFlow("₺0.00")
    val localPrice: StateFlow<String> = _localPrice.asStateFlow()

    fun getLocalItems() {
        viewModelScope.launch {
            getLocalItemsUseCase().collect { items ->
                _items.value = items
            }
        }
    }

    fun getTotalPrice() {
        viewModelScope.launch {
            getTotalPriceUseCase().collect { totalPrice ->
                _localPrice.value = totalPrice?.let { "₺${"%.2f".format(it)}" } ?: "₺0.00"
            }
        }
    }

    fun updateDataBase(product: Product) {
        viewModelScope.launch {
            insertDataBaseUseCase(product)
            getLocalItems()
            getTotalPrice()
            getDataBaseItemCount()
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            deleteAllItemsUseCase()
            getLocalItems()
            getTotalPrice()
            getDataBaseItemCount()
        }
    }

    fun getDataBaseItemCount() {
        viewModelScope.launch {
            getDataBaseItemCounter().collect { count ->
                _databaseCounter.value = count
            }
        }
    }
}