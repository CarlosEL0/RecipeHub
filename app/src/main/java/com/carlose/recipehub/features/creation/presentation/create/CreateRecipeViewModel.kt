package com.carlose.recipehub.features.creation.presentation.create

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class IngredientState(val id: Long = System.currentTimeMillis(), var name: String = "", var quantity: String = "")
data class StepState(val id: Long = System.currentTimeMillis(), var description: String = "")

@HiltViewModel
class CreateRecipeViewModel @Inject constructor() : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _time = MutableStateFlow("")
    val time = _time.asStateFlow()

    private val _portions = MutableStateFlow("")
    val portions = _portions.asStateFlow()

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

    fun addIngredient() {
        ingredients.add(IngredientState(id = System.nanoTime()))
    }

    fun removeIngredient(index: Int) {
        if (ingredients.size > 1) ingredients.removeAt(index)
    }

    fun updateIngredientName(index: Int, value: String) {
        ingredients[index] = ingredients[index].copy(name = value)
    }

    fun updateIngredientQuantity(index: Int, value: String) {
        ingredients[index] = ingredients[index].copy(quantity = value)
    }

    fun addStep() {
        steps.add(StepState(id = System.nanoTime()))
    }

    fun removeStep(index: Int) {
        if (steps.size > 1) steps.removeAt(index)
    }

    fun updateStepDescription(index: Int, value: String) {
        steps[index] = steps[index].copy(description = value)
    }
}