package com.carlose.recipehub.core.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
//import retrofit2.http.Query

data class LoginRequest(val email: String, val password: String)
data class AuthResponseDto(val id: Int, val name: String, val email: String, val token: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class UserResponseDto(val id: Int, val name: String, val email: String)
data class ImageUploadResponse(val url: String)

data class CreateRecipeRequest(
    val userId: Int,
    val title: String,
    val description: String,
    val preparationTime: Int,
    val portions: Int,
    val imageUrl: String,
    val categories: List<String>,
    val steps: List<String>,
    val ingredients: List<IngredientDto>
)

data class IngredientDto(val name: String, val quantity: String)
data class StepDto(val stepNumber: Int, val description: String)

data class RecipeResponseDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val preparationTime: Int,
    val portions: Int,
    val authorName: String,
    val authorId: Int,
    val publicationDate: String
)

data class RecipeDetailResponseDto(
    val id: Int,
    val title: String,
    val description: String,
    val preparationTime: Int,
    val portions: Int,
    val imageUrl: String?,
    val authorName: String,
    val publicationDate: String,
    val categories: List<String>,
    val ingredients: List<IngredientDto>,
    val steps: List<StepDto>
)

interface RecipeHubApiService {

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponseDto>

    @POST("api/v1/users")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponseDto>

    @GET("api/v1/recipes")
    suspend fun getAllRecipes(): Response<List<RecipeResponseDto>>

    @GET("api/v1/recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): Response<RecipeDetailResponseDto>

    @POST("api/v1/recipes")
    suspend fun createRecipe(@Body request: CreateRecipeRequest): Response<RecipeResponseDto>

    @Multipart
    @POST("api/v1/media/upload")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<ImageUploadResponse>

    @POST("api/v1/favorites/{id}")
    suspend fun toggleFavorite(
        @Path("id") recipeId: Int,
        @Query("userId") userId: Int
    ): Response<Map<String, Boolean>>
}