package com.carlose.recipehub.core.navigation

// Usamos sealed class para una navegación segura por tipos
sealed class Screen(val route: String) {

    // Auth Feature
    data object Login : Screen("login")
    data object SignUp : Screen("signup")

    // Main Features
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object CreateRecipe : Screen("create_recipe")
    data object Planner : Screen("planner")
    data object Profile : Screen("profile")

    // Detail Screen (Recibe argumentos)
    data object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: Int) = "recipe_detail/$recipeId"
    }
}