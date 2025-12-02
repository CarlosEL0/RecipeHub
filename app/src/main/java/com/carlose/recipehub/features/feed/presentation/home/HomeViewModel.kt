package com.carlose.recipehub.features.feed.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.features.feed.data.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            _recipes.value = repository.getRecipes()
            _isLoading.value = false
        }
    }

    fun onFavoriteClick(recipeId: Int) {
        viewModelScope.launch {
            updateLocalRecipeFavoriteStatus(recipeId)

            val result = repository.toggleFavorite(recipeId, userId = 1)

            result.onFailure {
                updateLocalRecipeFavoriteStatus(recipeId)
            }
        }
    }

    private fun updateLocalRecipeFavoriteStatus(recipeId: Int) {
        _recipes.value = _recipes.value.map { recipe ->
            if (recipe.id == recipeId) {
                recipe.copy(isFavorite = !recipe.isFavorite)
            } else {
                recipe
            }
        }
    }
}