package com.carlose.recipehub.features.planner.presentation.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlose.recipehub.core.model.MealPlanItem
import com.carlose.recipehub.features.planner.data.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
class PlannerViewModel @Inject constructor(
    private val repository: PlannerRepository
) : ViewModel() {

    private val _days = MutableStateFlow<List<DayState>>(emptyList())
    val days = _days.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _plannedMeals = MutableStateFlow<List<MealPlanItem>>(emptyList())
    val plannedMeals = _plannedMeals.asStateFlow()

    private var fullWeeklyPlan: List<MealPlanItem> = emptyList()

    init {
        generateDays()
        loadWeeklyPlan()
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
        filterMealsForSelectedDate()
    }

    private fun loadWeeklyPlan() {
        val start = LocalDate.now()
        val end = start.plusDays(14) // Cargamos 2 semanas

        viewModelScope.launch {
            val result = repository.getWeeklyPlan(start, end)
            result.onSuccess { items ->
                fullWeeklyPlan = items
                filterMealsForSelectedDate()
            }
        }
    }

    private fun filterMealsForSelectedDate() {
        val selected = _selectedDate.value.toString()
        _plannedMeals.value = fullWeeklyPlan.filter { it.date == selected }
    }
}