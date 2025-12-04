package com.carlose.recipehub.features.feed.data

import com.carlose.recipehub.core.model.Category
import com.carlose.recipehub.core.model.Ingredient
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.core.model.RecipeDetail
import com.carlose.recipehub.core.model.Step
import com.carlose.recipehub.core.network.RecipeHubApiService
import com.carlose.recipehub.core.network.RecipeDetailResponseDto
import javax.inject.Inject

class RecipeDetailRepository @Inject constructor(
    private val api: RecipeHubApiService
) {
    suspend fun getRecipeDetail(id: Int): Result<RecipeDetail> {
        return try {
            val response = api.getRecipeById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al cargar receta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun RecipeDetailResponseDto.toDomain(): RecipeDetail {
        val baseRecipe = Recipe(
            id = this.id,
            title = this.title,
            description = this.description,
            preparationTimeMinutes = this.preparationTime,
            portions = this.portions,
            imageUrl = this.imageUrl,
            publicationDate = this.publicationDate,
            authorName = this.authorName,
            authorId = 0,
            isFavorite = false
        )

        return RecipeDetail(
            recipe = baseRecipe,
            ingredients = this.ingredients.map { Ingredient(0, it.name, it.quantity) },
            steps = this.steps.map { Step(0, it.stepNumber, it.description) },
            categories = this.categories.map { Category(0, it) }
        )
    }

    suspend fun deleteRecipe(id: Int): Result<Boolean> {
        return try {
            val response = api.deleteRecipe(id)
            if (response.isSuccessful) Result.success(true) else Result.failure(Exception("Error al borrar"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}