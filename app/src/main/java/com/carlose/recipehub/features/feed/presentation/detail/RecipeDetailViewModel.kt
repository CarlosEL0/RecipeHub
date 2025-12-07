package com.carlose.recipehub.features.feed.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.MealType
import com.carlose.recipehub.core.model.RecipeDetail
import com.carlose.recipehub.core.network.CommentResponseDto
import com.carlose.recipehub.features.feed.data.CommentRepository
import com.carlose.recipehub.features.feed.data.RecipeDetailRepository
import com.carlose.recipehub.features.planner.data.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeDetailRepository,
    private val commentRepository: CommentRepository,
    private val plannerRepository: PlannerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _recipeDetail = MutableStateFlow<RecipeDetail?>(null)
    val recipeDetail = _recipeDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _comments = MutableStateFlow<List<CommentResponseDto>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _commentText = MutableStateFlow("")
    val commentText = _commentText.asStateFlow()

    private val _isSendingComment = MutableStateFlow(false)
    val isSendingComment = _isSendingComment.asStateFlow()

    // Variables para el Planificador
    private val _showPlannerDialog = MutableStateFlow(false)
    val showPlannerDialog = _showPlannerDialog.asStateFlow()

    private val _isAddingToPlan = MutableStateFlow(false)
    val isAddingToPlan = _isAddingToPlan.asStateFlow()

    // --- NUEVO: Estado para saber si se borró exitosamente ---
    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess = _deleteSuccess.asStateFlow()
    // --------------------------------------------------------

    private var currentRecipeId: Int? = null

    init {
        val recipeId = savedStateHandle.get<String>("recipeId")?.toIntOrNull()
        if (recipeId != null) {
            currentRecipeId = recipeId
            loadRecipe(recipeId)
            loadComments(recipeId)
        }
    }

    private fun loadRecipe(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getRecipeDetail(id)
            result.onSuccess { _recipeDetail.value = it }
            _isLoading.value = false
        }
    }

    private fun loadComments(id: Int) {
        viewModelScope.launch {
            val result = commentRepository.getComments(id)
            result.onSuccess { _comments.value = it }
        }
    }

    fun onCommentTextChanged(text: String) { _commentText.value = text }

    fun sendComment() {
        if (_commentText.value.isBlank() || currentRecipeId == null) return
        viewModelScope.launch {
            _isSendingComment.value = true
            val result = commentRepository.addComment(currentRecipeId!!, _commentText.value)
            result.onSuccess { newComment ->
                _comments.value = listOf(newComment) + _comments.value
                _commentText.value = ""
            }
            _isSendingComment.value = false
        }
    }

    // --- Funciones para el Planificador ---

    fun openPlannerDialog() {
        _showPlannerDialog.value = true
    }

    fun closePlannerDialog() {
        _showPlannerDialog.value = false
    }

    fun addToPlan(date: LocalDate, mealType: MealType) {
        if (currentRecipeId == null) return

        viewModelScope.launch {
            _isAddingToPlan.value = true
            val result = plannerRepository.addToPlan(currentRecipeId!!, date, mealType)

            result.onSuccess {
                _showPlannerDialog.value = false
            }
            _isAddingToPlan.value = false
        }
    }

    // --- NUEVO: Función para Eliminar Receta ---
    fun deleteRecipe() {
        if (currentRecipeId == null) return

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deleteRecipe(currentRecipeId!!)
            result.onSuccess {
                _deleteSuccess.value = true
            }
            // Si falla, quitamos el loading para que el usuario pueda reintentar
            _isLoading.value = false
        }
    }
}