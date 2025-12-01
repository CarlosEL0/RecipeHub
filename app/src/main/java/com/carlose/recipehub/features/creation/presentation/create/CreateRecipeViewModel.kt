package com.carlose.recipehub.features.creation.presentation.create

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.network.CreateRecipeRequest
import com.carlose.recipehub.core.network.IngredientDto
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
    @ApplicationContext private val context: Context
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

    init {
        addIngredient()
        addStep()
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

    fun publishRecipe() {
        if (_selectedImageUri.value == null) return

        viewModelScope.launch {
            _isLoading.value = true

            val file = FileUtil.getFileFromUri(context, _selectedImageUri.value!!)
            if (file != null) {
                val imageResult = repository.uploadImage(file)

                imageResult.onSuccess { imageUrl ->
                    val request = CreateRecipeRequest(
                        userId = 1,
                        title = _title.value,
                        description = _description.value,
                        preparationTime = _time.value.toIntOrNull() ?: 0,
                        portions = _portions.value.toIntOrNull() ?: 0,
                        imageUrl = imageUrl,
                        categories = listOf("General"),
                        steps = steps.map { it.description },
                        ingredients = ingredients.map { IngredientDto(it.name, it.quantity) }
                    )

                    val createResult = repository.createRecipe(request)
                    createResult.onSuccess {
                        _uploadSuccess.value = true
                        _isLoading.value = false
                    }.onFailure {
                        _isLoading.value = false
                    }
                }.onFailure {
                    _isLoading.value = false
                }
            } else {
                _isLoading.value = false
            }
        }
    }
}