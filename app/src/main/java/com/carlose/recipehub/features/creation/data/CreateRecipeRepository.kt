package com.carlose.recipehub.features.creation.data

import com.carlose.recipehub.core.network.CreateRecipeRequest
import com.carlose.recipehub.core.network.RecipeDetailResponseDto
import com.carlose.recipehub.core.network.RecipeHubApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CreateRecipeRepository @Inject constructor(
    private val api: RecipeHubApiService
) {

    suspend fun uploadImage(file: File): Result<String> {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = api.uploadImage(body)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.url)
            } else {
                Result.failure(Exception("Error al subir imagen"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRecipe(request: CreateRecipeRequest): Result<Boolean> {
        return try {
            val response = api.createRecipe(request)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al crear receta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRecipe(recipeId: Int, request: CreateRecipeRequest): Result<Boolean> {
        return try {
            val response = api.updateRecipe(recipeId, request)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error al actualizar receta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeForEdit(recipeId: Int): Result<RecipeDetailResponseDto> {
        return try {
            val response = api.getRecipeById(recipeId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar datos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
