package com.emarket.ui.chart

import com.emarket.data.local.ItemEntity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emarket.databinding.ProductBasketItemBinding
import com.emarket.utils.clickWithDebounce

class ProductBasketAdapter :
    ListAdapter<ItemEntity, ProductBasketAdapter.BasketViewHolder>(ChartDiffCallback()) {

    private var onItemClick: (ItemEntity) -> Unit = {}

    fun setOnItemClickListener(listener: (ItemEntity) -> Unit) {
        onItemClick = listener
    }

    inner class BasketViewHolder(private val binding: ProductBasketItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ItemEntity) {
            with(binding) {
                tvProductName.text = product.name
                tvPrice.text = product.price
                tvCounter.text = product.totalOrder.toString()

                btnPlus.clickWithDebounce { updateProductCount(product, 1) }
                btnMinus.clickWithDebounce { updateProductCount(product, -1) }
            }
        }

        private fun updateProductCount(product: ItemEntity, increment: Int) {
            product.totalOrder += increment
            binding.tvCounter.text = product.totalOrder.toString()
            onItemClick.invoke(product)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val binding = ProductBasketItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ChartDiffCallback : DiffUtil.ItemCallback<ItemEntity>() {
        override fun areItemsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
            return oldItem == newItem
        }
    }
}