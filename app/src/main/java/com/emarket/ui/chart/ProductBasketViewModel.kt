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
    val items: StateFlow<List<ItemEntity>> = _items

    private val _databaseCounter = MutableStateFlow(0)
    val databaseCounter: MutableStateFlow<Int> = _databaseCounter

    private val _localPrice = MutableStateFlow("₺0.00")
    val localPrice = _localPrice.asStateFlow()

    fun getLocalItems() {
        viewModelScope.launch {
            getLocalItemsUseCase().collect {
                _items.value = it
            }
        }
    }

    fun getTotalPrice() {
        viewModelScope.launch {
            getTotalPriceUseCase().collect {
                it?.let {
                    _localPrice.value = "₺" + String.format("%.2f", it)
                }
                if (it == null) _localPrice.value = "₺0.00"
            }
        }
    }

    fun updateDataBase(item: Product) {
        viewModelScope.launch {
            insertDataBaseUseCase(item)
            getLocalItems()
            getTotalPrice()
        }
        getDataBaseItemCount()

    }

    fun clearDatabase() {
        viewModelScope.launch {
            deleteAllItemsUseCase()
            getLocalItems()
            getTotalPrice()
        }
        getDataBaseItemCount()
    }
    fun getDataBaseItemCount(){
        viewModelScope.launch {

            getDataBaseItemCounter().collect{
            getDataBaseItemCounter().collect {

            getDataBaseItemCounter().collect {
                _databaseCounter.value = it
            }
        }
    }

}