package com.emarket.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.io.IOException

class PagingDataSource(
    private val serviceInterface: ServiceInterface,
    private val selectedBrands: String, // Parameter for selected brands
    private val selectedModels: String, // Parameter for selected models
    private val selectedSortBy: String  // Added parameter for selected sort option
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        delay(1000)
        val pageIndex = params.key ?: 1
        return try {
            val response = serviceInterface.fetchProducts(pageIndex, params.loadSize, selectedBrands, selectedModels, selectedSortBy)

            if (response.isSuccessful) {
                val productList = response.body() ?: emptyList()

                if (productList.isEmpty()) {
                    return LoadResult.Page(
                        data = listOf(),
                        prevKey = if (pageIndex == 1) null else pageIndex - 1,
                        nextKey = null // No more pages
                    )
                }

                LoadResult.Page(
                    data = productList,
                    prevKey = if (pageIndex == 1) null else pageIndex - 1,
                    nextKey = pageIndex + 1
                )
            } else {
                if (response.code() == 404) {
                    // Log a specific message for 404
                    return LoadResult.Error(Exception("Veri Bulunamadı"))
                } else {
                    // Log general HTTP error
                    Log.e("Error", "HTTP error: ${response.code()} - ${response.message()}")
                    return LoadResult.Error(Exception("HTTP error: ${response.code()}"))
                }
            }
        } catch (e: IOException) {
            Log.e("Error", "IOException: $e")
            LoadResult.Error(e)
        }
    }

}
