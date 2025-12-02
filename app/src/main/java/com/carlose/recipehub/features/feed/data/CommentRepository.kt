package com.carlose.recipehub.features.feed.data

import com.carlose.recipehub.core.network.CommentRequest
import com.carlose.recipehub.core.network.CommentResponseDto
import com.carlose.recipehub.core.network.RecipeHubApiService
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val api: RecipeHubApiService
) {
    suspend fun getComments(recipeId: Int): Result<List<CommentResponseDto>> {
        return try {
            val response = api.getComments(recipeId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar comentarios"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addComment(recipeId: Int, text: String): Result<CommentResponseDto> {
        return try {
            val request = CommentRequest(userId = 1, text = text)
            val response = api.addComment(recipeId, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al publicar comentario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}