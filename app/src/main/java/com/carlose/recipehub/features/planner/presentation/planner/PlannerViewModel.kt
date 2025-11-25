package com.carlose.recipehub.features.planner.presentation.planner

import androidx.lifecycle.ViewModel
import com.carlose.recipehub.core.model.MealPlanItem
import com.carlose.recipehub.core.model.MealType
import com.carlose.recipehub.core.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class DayState(
    val date: LocalDate,
    val dayName: String,
    val dayNumber: String,
    val isSelected: Boolean = false
)

@HiltViewModel
class PlannerViewModel @Inject constructor() : ViewModel() {

    private val _days = MutableStateFlow<List<DayState>>(emptyList())
    val days = _days.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _plannedMeals = MutableStateFlow<List<MealPlanItem>>(emptyList())
    val plannedMeals = _plannedMeals.asStateFlow()

    init {
        generateDays()
        loadMockData()
    }

    private fun generateDays() {
        val today = LocalDate.now()
        val daysList = (0..13).map { i ->
            val date = today.plusDays(i.toLong())
            DayState(
                date = date,
                dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES")).uppercase(),
                dayNumber = date.dayOfMonth.toString(),
                isSelected = i == 0
            )
        }
        _days.value = daysList
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _days.value = _days.value.map { it.copy(isSelected = it.date == date) }
        // Aquí filtraríamos las comidas reales por fecha
    }

    private fun loadMockData() {
        // Datos falsos para probar la UI
        val today = LocalDate.now().toString()

        val mockRecipe = Recipe(
            id = 1,
            title = "Avena con Frutas",
            description = "Desayuno",
            preparationTimeMinutes = 15,
            portions = 1,
            imageUrl = "https://images.unsplash.com/photo-1517673132405-a56a62b18caf?q=80&w=1000",
            publicationDate = "2025-01-01",
            authorName = "Yo",
            authorId = 1
        )

        _plannedMeals.value = listOf(
            MealPlanItem(1, today, MealType.BREAKFAST, mockRecipe),
            MealPlanItem(2, today, MealType.LUNCH, mockRecipe.copy(title = "Pollo con Verduras", imageUrl = "https://images.unsplash.com/photo-1588315029754-2dd089d39a1a?q=80&w=1000")),
            MealPlanItem(3, today, MealType.DINNER, mockRecipe.copy(title = "Ensalada Ligera", imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=1000"))
        )
    }
}