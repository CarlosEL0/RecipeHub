package com.carlose.recipehub.core.model

import java.util.Date

/**
 * --- MODELOS DE DOMINIO ---
 * Estas clases representan los datos puros de la aplicación.
 * Se usarán en la UI, Base de Datos y Red.
 */

// 1. USUARIO
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val profilePictureUrl: String?,
    val token: String? = null // Token JWT para autenticación
)

// 2. RECETA (Resumen para feeds)
data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val preparationTimeMinutes: Int,
    val portions: Int,
    val imageUrl: String?, // Puede ser null si no hay imagen
    val publicationDate: String, // Usamos String por facilidad inicial con JSON
    val authorName: String,
    val authorId: Int,
    val isFavorite: Boolean = false // Estado local
)

// 3. PASO DE RECETA
data class Step(
    val id: Int,
    val stepNumber: Int,
    val description: String
)

// 4. INGREDIENTE
data class Ingredient(
    val id: Int,
    val name: String,
    val quantity: String
)

// 5. DETALLE COMPLETO DE RECETA
data class RecipeDetail(
    val recipe: Recipe,
    val ingredients: List<Ingredient>,
    val steps: List<Step>,
    val categories: List<Category>
)

// 6. CATEGORÍA
data class Category(
    val id: Int,
    val name: String,
    val iconUrl: String? = null
)

// 7. PLANIFICADOR (Calendario)
enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK
}

data class MealPlanItem(
    val id: Int = 0, // 0 para autogenerar en DB local
    val date: String, // Formato YYYY-MM-DD
    val mealType: MealType,
    val recipe: Recipe
)