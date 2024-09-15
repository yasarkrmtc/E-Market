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

    private var itemClick: (item: Product) -> Unit = { item -> }

    fun itemClick(item: (Product) -> Unit) {
        itemClick = item
    }

    private var productClick: (item: Product) -> Unit = { item -> }

    fun productClick(item: (Product) -> Unit) {
        productClick = item
    }

    private var favoriteClick: (item: Product) -> Unit = { item -> }

    fun favoriteClick(item: (Product) -> Unit) {
        favoriteClick = item
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
            binding.productName.text = product.name
            binding.productPrice.text = product.price

            binding.favoriteIcon.isSelected = product.isFavorite

            binding.favoriteIcon.clickWithDebounce {
                product.isFavorite = !product.isFavorite
                favoriteClick.invoke(product)

                binding.favoriteIcon.isSelected = product.isFavorite
            }

            Glide.with(binding.root.context)
                .load(product.image)
                .into(binding.productImage)

            binding.addToCartButton.clickWithDebounce {
                product.totalOrder += 1
                itemClick.invoke(product)
                Toast.makeText(binding.root.context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            }

            binding.root.clickWithDebounce {
                productClick.invoke(product)
            }
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