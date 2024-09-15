package com.emarket.ui.filters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View

import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

        val brandItems = brands
        val modelItems = models

        binding.apply {
            filtersCloseButton.clickWithDebounce {
                findNavController().popBackStack()
            }

            brandRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            val brandAdapter = FiltersAdapter { item, isChecked ->
                if (isChecked) {
                    selectedBrands.add(item)
                } else {
                    selectedBrands.remove(item)
                }
            }
            binding.brandRecyclerview.adapter = brandAdapter
            brandAdapter.submitList(brandItems)
            brandSearchbar.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newText = s.toString().lowercase()
                    val filteredList = brands.filter { it.lowercase().contains(newText) }
                    brandAdapter.submitList(filteredList)
                    if (newText.isEmpty()) {
                        binding.brandRecyclerview.scrollToPosition(0)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            filterModelRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            val modelAdapter = FiltersAdapter { item, isChecked ->
                if (isChecked) {
                    selectedModels.add(item)
                } else {
                    selectedModels.remove(item)
                }
            }
            binding.filterModelRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            binding.filterModelRecyclerview.adapter = modelAdapter
            modelAdapter.submitList(modelItems)
            filterModelSearchbar.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newText = s.toString().lowercase()
                    val filteredList = modelItems.filter { it.lowercase().contains(newText) }
                    modelAdapter.submitList(filteredList)
                    if (newText.isEmpty()) {
                        binding.filterModelRecyclerview.scrollToPosition(0)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.filterPrimaryButton.clickWithDebounce {
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

                    putString("selectedSortBy", selectedSortBy)
                }

                    putString("selectedSortBy", selectedSortBy)
                }

                setFragmentResult("filterResult", bundle)
                findNavController().popBackStack()
            }
        }
    }
}


