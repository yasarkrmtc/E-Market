package com.emarket.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.emarket.data.remote.PagingDataSource
import com.emarket.data.remote.Product
import com.emarket.data.remote.ServiceInterface
import com.emarket.domain.usecase.InsertDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val serviceInterface: ServiceInterface,
    private val insertDataBaseUseCase: InsertDataBaseUseCase
) : ViewModel() {
    init {
        savedStateHandle["PAGING_ID"] = 0
    }
    private val clearListCh = Channel<Unit>(Channel.CONFLATED)
    fun getProducts(): Flow<PagingData<Product>> {
        @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
        return flowOf(
            clearListCh.receiveAsFlow().map { PagingData.empty() },
            savedStateHandle.getLiveData<Int>("PAGING_ID")
                .asFlow()
                .flatMapLatest {
                    Pager(
                        PagingConfig(1)
                    ) {
                        PagingDataSource(
                            serviceInterface = serviceInterface,
                        )
                    }.flow
                }
        ).flattenMerge(2)
    }
    fun updateDataBase(item: Product) {
        viewModelScope.launch {
            insertDataBaseUseCase(item)
        }
    }
}
