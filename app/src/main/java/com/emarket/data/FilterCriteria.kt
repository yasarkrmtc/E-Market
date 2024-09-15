package com.emarket.data

data class FilterCriteria(
    val query: String,
    val brands: String,
    val models: String,
    val sortBy: String
)