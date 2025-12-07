package com.carlose.recipehub.features.profile.data

import android.se.omapi.Session
import com.carlose.recipehub.core.model.Recipe
import com.carlose.recipehub.core.network.RecipeHubApiService
import com.carlose.recipehub.core.network.RecipeResponseDto
import com.carlose.recipehub.core.session.SessionManager
import javax.inject.Inject
class ProfileRepository @Inject constructor(
    private val api: RecipeHubApiService
) {
    suspend fun getMyRecipes(): Result<List<Recipe>> {
        return try {
            // Usamos userId = 1 hardcoded por ahora
            val currentUserId = SessionManager.getUserId()
            if (currentUserId == -1) return Result.failure(Exception("No hay sesión"))

            val response = api.getMyRecipes(userId = currentUserId)

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