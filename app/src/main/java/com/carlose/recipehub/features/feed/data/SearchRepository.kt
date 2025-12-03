package com.carlose.recipehub.features.feed.data

import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.core.network.RecipeHubApiService
import com.carlose.recipehub.core.network.RecipeResponseDto
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: RecipeHubApiService
) {
    suspend fun searchRecipes(query: String): Result<List<Recipe>> {
        return try {
            val response = api.searchRecipes(query)
            if (response.isSuccessful && response.body() != null) {
                val recipes = response.body()!!.map { it.toDomain() }
                Result.success(recipes)
            } else {
                Result.success(emptyList())
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