package com.emarket.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class PagingDataSource(
    private val serviceInterface: ServiceInterface,
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val pageIndex = params.key ?: 1
        return try {
            // Artık suspend fonksiyon kullanıldığı için ağ işlemi arka planda yapılacak
            val productList = serviceInterface.fetchProducts()

            Log.d("ProductResponse", productList.toString())

            if (productList.isEmpty()) {
                return LoadResult.Page(
                    data = listOf(),
                    prevKey = if (pageIndex == 1) null else pageIndex - 1,
                    nextKey = null // Daha fazla sayfa yok
                )
            }

            LoadResult.Page(
                data = productList,
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
                nextKey = pageIndex + 1
            )
        } catch (e: IOException) {
            Log.e("Error", "IOException: $e")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("Error", "HttpException: $e")
            LoadResult.Error(e)
        }
    }
}

