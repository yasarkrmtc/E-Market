package com.emarket.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emarket.data.local.FavoriteItemEntity
import com.emarket.databinding.FavoriteFragmentItemBinding

class FavoriteAdapter(
    private var favoriteList: List<FavoriteItemEntity> = listOf(),
    private val onDeleteClick: (FavoriteItemEntity) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteFragmentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val product = favoriteList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = favoriteList.size

    // Method to update the list
    fun updateList(newList: List<FavoriteItemEntity>) {
        favoriteList = newList
        notifyDataSetChanged() // Consider using DiffUtil for more efficient updates
    }

    inner class FavoriteViewHolder(private val binding: FavoriteFragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: FavoriteItemEntity) {
            binding.productName.text = product.name
            binding.productPrice.text = product.price
            Glide.with(binding.root.context)
                .load(product.image)
                .into(binding.productImage)

            binding.deleteFavoriteButton.setOnClickListener {
                onDeleteClick.invoke(product)
            }
        }
    }
}

