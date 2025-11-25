package com.carlose.recipehub.features.feed.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.features.feed.data.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todo")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    val categories = listOf("Todo", "Desayuno", "Almuerzo", "Cena", "Postres", "Vegano", "Rápido", "Bebidas")

    private var allRecipes: List<Recipe> = emptyList()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            allRecipes = repository.getRecipes() + repository.getRecipes()
            filterResults()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterResults()
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        filterResults()
    }

    private fun filterResults() {
        val query = _searchQuery.value.lowercase()
        val category = _selectedCategory.value

        _searchResults.value = allRecipes.filter { recipe ->
            val matchesSearch = recipe.title.lowercase().contains(query) ||
                    recipe.description.lowercase().contains(query)

            val matchesCategory = if (category == "Todo") true else {

                when(category) {
                    "Desayuno" -> recipe.title.contains("Avena") || recipe.title.contains("Bowl")
                    "Almuerzo" -> recipe.title.contains("Pasta") || recipe.title.contains("Pollo")
                    else -> true
                }
            }

            matchesSearch && matchesCategory
        }
    }
}