package com.emarket.ui.chart


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emarket.data.local.ItemEntity
import com.emarket.data.remote.Product
import com.emarket.domain.usecase.DeleteAllItemsUseCase
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
) : ViewModel() {
    private val _items = MutableStateFlow<List<ItemEntity>>(emptyList())
    val items: StateFlow<List<ItemEntity>> = _items

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

    }

    fun clearDatabase() {
        viewModelScope.launch {
            deleteAllItemsUseCase()
            getLocalItems()
            getTotalPrice()
        }
    }

}