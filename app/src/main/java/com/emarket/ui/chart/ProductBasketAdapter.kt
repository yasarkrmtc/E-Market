package com.emarket.ui.chart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emarket.data.remote.Product
import com.emarket.databinding.ProductBasketItemBinding

class ProductBasketAdapter(
    private val items: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onQuantityChanged: (Product, Int) -> Unit
) : RecyclerView.Adapter<ProductBasketAdapter.BasketViewHolder>() {

    inner class BasketViewHolder(private val binding: ProductBasketItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvPrice.text = product.price
            binding.tvCounter.text = product.totalOrder.toString()

            binding.btnMinus.setOnClickListener {
                if (product.totalOrder > 1) {
                    product.totalOrder -= 1
                    binding.tvCounter.text = product.totalOrder.toString()
                    onQuantityChanged(product, product.totalOrder)
                }
            }

            binding.btnPlus.setOnClickListener {
                product.totalOrder += 1
                binding.tvCounter.text = product.totalOrder.toString()
                onQuantityChanged(product, product.totalOrder)
            }

            binding.root.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val binding = ProductBasketItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
