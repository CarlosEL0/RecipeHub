package com.carlose.recipehub.features.feed.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.features.feed.data.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Categorías estáticas para filtrar visualmente (Feature futura: filtrar en backend por categoría)
    val categories = listOf("Todo", "Desayuno", "Almuerzo", "Cena", "Postres", "Vegano")
    private val _selectedCategory = MutableStateFlow("Todo")
    val selectedCategory = _selectedCategory.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // Cancelamos la búsqueda anterior si el usuario sigue escribiendo rápido
        searchJob?.cancel()

        if (query.length > 2) { // Solo buscamos si hay más de 2 letras
            searchJob = viewModelScope.launch {
                delay(500) // Esperamos 500ms a que termine de escribir (Debounce)
                performSearch(query)
            }
        } else {
            _searchResults.value = emptyList()
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        // Aquí podrías agregar lógica para filtrar los resultados actuales
    }

    private suspend fun performSearch(query: String) {
        _isLoading.value = true
        val result = repository.searchRecipes(query)
        result.onSuccess {
            _searchResults.value = it
        }
        _isLoading.value = false
    }
}