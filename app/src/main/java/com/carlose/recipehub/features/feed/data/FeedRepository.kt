package com.carlose.recipehub.features.feed.data

import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.core.network.RecipeHubApiService
import com.carlose.recipehub.core.network.RecipeResponseDto
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val api: RecipeHubApiService
) {

    suspend fun getRecipes(): List<Recipe> {
        return try {
            val response = api.getAllRecipes(userId = 1)

            if (response.isSuccessful && response.body() != null) {
                response.body()!!.map { dto -> dto.toDomain() }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun toggleFavorite(recipeId: Int, userId: Int): Result<Boolean> {
        return try {
            val response = api.toggleFavorite(recipeId, userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!["isFavorite"] ?: false)
            } else {
                Result.failure(Exception("Error al actualizar favorito"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun RecipeResponseDto.toDomain(): Recipe {
        return Recipe(
            id = this.id,
            title = this.title,
            description = this.description,
            preparationTimeMinutes = this.preparationTime,
            portions = this.portions,
            imageUrl = this.imageUrl,
            publicationDate = this.publicationDate,
            authorName = this.authorName,
            authorId = this.authorId,
            isFavorite = this.isFavorite
        )
    }
}