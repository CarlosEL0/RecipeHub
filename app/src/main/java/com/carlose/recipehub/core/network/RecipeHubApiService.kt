package com.carlose.recipehub.core.network

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    val publicationDate: String,
    @SerializedName("isFavorite")
    val isFavorite: Boolean = false
)

data class RecipeDetailResponseDto(
    val id: Int,
    val title: String,
    val description: String,
    val preparationTime: Int,
    val portions: Int,
    val imageUrl: String?,
    val authorName: String,
    val authorId: Int,
    val publicationDate: String,
    val categories: List<String>,
    val ingredients: List<IngredientDto>,
    val steps: List<StepDto>
)

data class MealPlanRequest(
    val userId: Int,
    val recipeId: Int,
    val date: String,
    val mealType: String
)

data class MealPlanResponseDto(
    val id: Int,
    val date: String,
    val mealType: String,
    val recipeId: Int,
    val recipeTitle: String,
    val recipeImageUrl: String?,
    val preparationTime: Int
)

data class CommentRequest(val userId: Int, val text: String)

data class CommentResponseDto(
    val id: Int,
    val text: String,
    val authorName: String,
    val authorProfileUrl: String?,
    val createdAt: String
)

interface RecipeHubApiService {

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponseDto>

    @POST("api/v1/users")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponseDto>

    @GET("api/v1/recipes")
    suspend fun getAllRecipes(@Query("userId") userId: Int): Response<List<RecipeResponseDto>>

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

    @POST("api/v1/planner")
    suspend fun addToPlan(@Body request: MealPlanRequest): Response<MealPlanResponseDto>

    @GET("api/v1/planner")
    suspend fun getWeeklyPlan(
        @Query("userId") userId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<MealPlanResponseDto>>

    @retrofit2.http.DELETE("api/v1/planner/{id}")
    suspend fun removeFromPlan(@Path("id") planId: Int): Response<Void>

    @GET("api/v1/recipes/{id}/comments")
    suspend fun getComments(@Path("id") recipeId: Int): Response<List<CommentResponseDto>>

    @POST("api/v1/recipes/{id}/comments")
    suspend fun addComment(@Path("id") recipeId: Int, @Body request: CommentRequest): Response<CommentResponseDto>

    @GET("api/v1/recipes/search")
    suspend fun searchRecipes(@Query("query") query: String): Response<List<RecipeResponseDto>>

    @GET("api/v1/recipes/author/{userId}")
    suspend fun getMyRecipes(@Path("userId") userId: Int): Response<List<RecipeResponseDto>>

    // ...
    @retrofit2.http.PUT("api/v1/recipes/{id}")
    suspend fun updateRecipe(@Path("id") id: Int, @Body request: CreateRecipeRequest): Response<RecipeResponseDto>

    @retrofit2.http.DELETE("api/v1/recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: Int): Response<Void>
}