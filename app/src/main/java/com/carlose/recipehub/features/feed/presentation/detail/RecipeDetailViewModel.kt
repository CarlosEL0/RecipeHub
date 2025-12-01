package com.carlose.recipehub.features.feed.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.RecipeDetail
import com.carlose.recipehub.features.feed.data.RecipeDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _recipeDetail = MutableStateFlow<RecipeDetail?>(null)
    val recipeDetail = _recipeDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        val recipeId = savedStateHandle.get<String>("recipeId")?.toIntOrNull()
        if (recipeId != null) {
            loadRecipe(recipeId)
        }
    }

    private fun loadRecipe(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getRecipeDetail(id)
            result.onSuccess {
                _recipeDetail.value = it
            }
            _isLoading.value = false
        }
    }
}