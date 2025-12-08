package com.carlose.recipehub.features.creation.presentation.create

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.network.CreateRecipeRequest
import com.carlose.recipehub.core.network.IngredientDto
import com.carlose.recipehub.core.session.SessionManager
import com.carlose.recipehub.core.network.RecipeDetailResponseDto
import com.carlose.recipehub.core.util.FileUtil
import com.carlose.recipehub.features.creation.data.CreateRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class IngredientState(val id: Long = System.currentTimeMillis(), var name: String = "", var quantity: String = "")
data class StepState(val id: Long = System.currentTimeMillis(), var description: String = "")

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val repository: CreateRecipeRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _time = MutableStateFlow("")
    val time = _time.asStateFlow()

    private val _portions = MutableStateFlow("")
    val portions = _portions.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess = _uploadSuccess.asStateFlow()

    val ingredients = mutableStateListOf<IngredientState>()
    val steps = mutableStateListOf<StepState>()

    private var editingRecipeId: Int? = null

    init {
        if (ingredients.isEmpty()) addIngredient()
        if (steps.isEmpty()) addStep()
    }

    init {
        val recipeId = savedStateHandle.get<String>("recipeId")?.toIntOrNull()
        if (recipeId != null) {
            editingRecipeId = recipeId
            loadRecipeForEdit(recipeId)
        } else {
            // Modo Crear: Inicializamos listas vacías
            if (ingredients.isEmpty()) addIngredient()
            if (steps.isEmpty()) addStep()
        }
    }

    fun onTitleChange(value: String) { _title.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    fun onTimeChange(value: String) { _time.value = value }
    fun onPortionsChange(value: String) { _portions.value = value }
    fun onImageSelected(uri: Uri?) { _selectedImageUri.value = uri }

    fun addIngredient() { ingredients.add(IngredientState(id = System.nanoTime())) }
    fun removeIngredient(index: Int) { if (ingredients.size > 1) ingredients.removeAt(index) }
    fun updateIngredientName(index: Int, value: String) { ingredients[index] = ingredients[index].copy(name = value) }
    fun updateIngredientQuantity(index: Int, value: String) { ingredients[index] = ingredients[index].copy(quantity = value) }

    fun addStep() { steps.add(StepState(id = System.nanoTime())) }
    fun removeStep(index: Int) { if (steps.size > 1) steps.removeAt(index) }
    fun updateStepDescription(index: Int, value: String) { steps[index] = steps[index].copy(description = value) }

    private fun loadRecipeForEdit(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getRecipeForEdit(id)

            result.onSuccess { dto ->
                _title.value = dto.title
                _description.value = dto.description
                _time.value = dto.preparationTime.toString()
                _portions.value = dto.portions.toString()

                ingredients.clear()
                dto.ingredients.forEach {
                    ingredients.add(IngredientState(name = it.name, quantity = it.quantity))
                }

                steps.clear()
                dto.steps.forEach {
                    steps.add(StepState(description = it.description))
                }
            }
            _isLoading.value = false
        }
    }

    fun publishRecipe() {
        if (_title.value.isBlank()) {
            showToast("El título es obligatorio")
            return
        }

        val timeInt = _time.value.toIntOrNull()
        if (timeInt == null || timeInt <= 0) {
            showToast("El tiempo debe ser un número válido (minutos)")
            return
        }

        val portionsInt = _portions.value.toIntOrNull() ?: 1

        viewModelScope.launch {
            _isLoading.value = true

            var imageUrl = ""

            if (_selectedImageUri.value != null) {
                val file = FileUtil.getFileFromUri(context, _selectedImageUri.value!!)
                if (file != null) {
                    val imageResult = repository.uploadImage(file)
                    if (imageResult.isSuccess) imageUrl = imageResult.getOrDefault("")
                }
            }

            val request = CreateRecipeRequest(
                userId = SessionManager.getUserId(),
                title = _title.value,
                description = _description.value,
                preparationTime = timeInt,
                portions = portionsInt,
                imageUrl = imageUrl,
                categories = listOf("General"),
                steps = steps.map { it.description }.filter { it.isNotBlank() },
                ingredients = ingredients.map { IngredientDto(it.name, it.quantity) }.filter { it.name.isNotBlank() }
            )

            val result = if (editingRecipeId != null) {
                repository.updateRecipe(editingRecipeId!!, request)
            } else {
                repository.createRecipe(request)
            }

            result.onSuccess {
                showToast(if (editingRecipeId != null) "Receta actualizada" else "Receta publicada")
                _uploadSuccess.value = true
            }.onFailure {
                showToast("Error: ${it.message}")
            }

            _isLoading.value = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}