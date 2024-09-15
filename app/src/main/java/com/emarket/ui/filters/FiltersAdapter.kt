package com.emarket.ui.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emarket.databinding.FiltersPageItemBinding


class FiltersAdapter(
    private val onItemCheckedChanged: (String, Boolean) -> Unit
) : ListAdapter<String, FiltersAdapter.FilterViewHolder>(FilterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = FiltersPageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FilterViewHolder(private val binding: FiltersPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.checkbox.text = item
            binding.checkbox.setOnCheckedChangeListener(null) // Prevent recycling issues
            binding.checkbox.isChecked = false // Default to unchecked, modify as needed

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onItemCheckedChanged(item, isChecked)
            }
        }
    }

    class FilterDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            // Assuming the items are unique strings, modify if needed
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}

