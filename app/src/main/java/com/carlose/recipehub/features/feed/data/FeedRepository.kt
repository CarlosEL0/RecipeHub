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
            val response = api.getAllRecipes()

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
            isFavorite = false
        )
    }
}