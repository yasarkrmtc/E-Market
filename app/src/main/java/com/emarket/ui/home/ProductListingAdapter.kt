package com.emarket.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emarket.data.remote.Product
import com.emarket.databinding.ProductListiningItemBinding
import com.emarket.utils.clickWithDebounce

class ProductListingAdapter :
    PagingDataAdapter<Product, ProductListingAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    private var onItemClick: (item: Product) -> Unit = {}
    private var onProductClick: (item: Product) -> Unit = {}
    private var onFavoriteClick: (item: Product) -> Unit = {}

    fun setItemClickListener(listener: (Product) -> Unit) {
        onItemClick = listener
    }

    fun setProductClickListener(listener: (Product) -> Unit) {
        onProductClick = listener
    }

    fun setFavoriteClickListener(listener: (Product) -> Unit) {
        onFavoriteClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductListiningItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        product?.let {
            holder.bind(it)
        }
    }

    inner class ProductViewHolder(private val binding: ProductListiningItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                productName.text = product.name
                productPrice.text = product.price
                favoriteIcon.isSelected = product.isFavorite

                Glide.with(root.context)
                    .load(product.image)
                    .into(productImage)

                favoriteIcon.clickWithDebounce {
                    toggleFavorite(product)
                }

                addToCartButton.clickWithDebounce {
                    addToCart(product)
                }

                root.clickWithDebounce {
                    onProductClick.invoke(product)
                }
            }
        }

        private fun toggleFavorite(product: Product) {
            product.isFavorite = !product.isFavorite
            onFavoriteClick.invoke(product)
            binding.favoriteIcon.isSelected = product.isFavorite
        }

        private fun addToCart(product: Product) {
            product.totalOrder += 1
            onItemClick.invoke(product)
            Toast.makeText(binding.root.context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}
