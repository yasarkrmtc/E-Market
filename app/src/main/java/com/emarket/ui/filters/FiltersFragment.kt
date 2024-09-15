package com.emarket.ui.filters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emarket.R
import com.emarket.base.BaseFragment
import com.emarket.databinding.FragmentFiltersBinding
import com.emarket.utils.Constants.brands
import com.emarket.utils.Constants.models
import com.emarket.utils.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FiltersFragment :
    BaseFragment<FragmentFiltersBinding>(FragmentFiltersBinding::inflate) {

    private val selectedBrands = mutableListOf<String>()
    private val selectedModels = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupSearchListeners()
    }

    private fun setupUI() {
     //   binding.filtersCloseButton.clickWithDebounce { findNavController().popBackStack() }

        setupRecyclerView(binding.brandRecyclerview, brands, selectedBrands)
        setupRecyclerView(binding.filterModelRecyclerview, models, selectedModels)

        binding.filterPrimaryButton.clickWithDebounce { applyFilters() }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        items: List<String>,
        selectedItems: MutableList<String>
    ) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FiltersAdapter { item, isChecked ->
            if (isChecked) {
                selectedItems.add(item)
            } else {
                selectedItems.remove(item)
            }
        }
        recyclerView.adapter = adapter
        adapter.submitList(items)
    }

    private fun setupSearchListeners() {
        setupSearchListener(binding.brandSearchbar, brands) { filteredList ->
            (binding.brandRecyclerview.adapter as FiltersAdapter).submitList(filteredList)
        }

        setupSearchListener(binding.filterModelSearchbar, models) { filteredList ->
            (binding.filterModelRecyclerview.adapter as FiltersAdapter).submitList(filteredList)
        }
    }

    private fun setupSearchListener(
        searchView: EditText,
        items: List<String>,
        onResultsFiltered: (List<String>) -> Unit
    ) {
        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newText = s.toString().lowercase()
                val filteredList = items.filter { it.lowercase().contains(newText) }
                onResultsFiltered(filteredList)
                if (newText.isEmpty()) {
                    binding.brandRecyclerview.scrollToPosition(0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun applyFilters() {
        val selectedSortBy = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.old_to_new -> "createdAt"
            R.id.new_to_old -> "-createdAt"
            R.id.price_low_to_high -> "price"
            R.id.price_high_to_low -> "-price"
            else -> ""
        }

        val bundle = Bundle().apply {
            putStringArrayList("selectedBrands", ArrayList(selectedBrands))
            putStringArrayList("selectedModels", ArrayList(selectedModels))
            putString("selectedSortBy", selectedSortBy)
        }

        setFragmentResult("filterResult", bundle)
        findNavController().popBackStack()
    }
}
