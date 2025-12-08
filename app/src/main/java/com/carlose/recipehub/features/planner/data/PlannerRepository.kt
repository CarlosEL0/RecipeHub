package com.carlose.recipehub.features.planner.data

import com.carlose.recipehub.core.model.MealPlanItem
import com.carlose.recipehub.core.model.MealType
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.core.network.MealPlanRequest
import com.carlose.recipehub.core.network.RecipeHubApiService
import com.carlose.recipehub.core.session.SessionManager
import java.time.LocalDate
import javax.inject.Inject

class PlannerRepository @Inject constructor(
    private val api: RecipeHubApiService
) {

    suspend fun getWeeklyPlan(startDate: LocalDate, endDate: LocalDate): Result<List<MealPlanItem>> {
        return try {
            val userId = SessionManager.getUserId()
            if (userId == -1) return Result.failure(Exception("Sesión inválida"))

            val response = api.getWeeklyPlan(
                userId = userId,
                startDate = startDate.toString(),
                endDate = endDate.toString()
            )

            if (response.isSuccessful && response.body() != null) {
                val domainItems = response.body()!!.map { dto ->
                    MealPlanItem(
                        id = dto.id,
                        date = dto.date,
                        mealType = MealType.valueOf(dto.mealType),
                        recipe = Recipe(
                            id = dto.recipeId,
                            title = dto.recipeTitle,
                            description = "",
                            preparationTimeMinutes = dto.preparationTime,
                            portions = 0,
                            imageUrl = dto.recipeImageUrl,
                            publicationDate = "",
                            authorName = "",
                            authorId = 0
                        )
                    )
                }
                Result.success(domainItems)
            } else {
                Result.failure(Exception("Error al cargar plan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToPlan(recipeId: Int, date: LocalDate, mealType: MealType): Result<Boolean> {
        return try {
            val userId = SessionManager.getUserId()

            val request = MealPlanRequest(
                userId = userId,
                recipeId = recipeId,
                date = date.toString(),
                mealType = mealType.name
            )
            val response = api.addToPlan(request)
            if (response.isSuccessful) Result.success(true) else Result.failure(Exception("Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}